/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.ui.draganddrop

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.TraversableNode
import androidx.compose.ui.node.TraversableNode.Companion.TraverseDescendantsAction
import androidx.compose.ui.node.requireLayoutNode
import androidx.compose.ui.node.requireOwner
import androidx.compose.ui.node.traverseChildren
import androidx.compose.ui.node.traverseDescendants

/**
 * A [Modifier.Node] providing low level access to platform drag and drop operations.
 * In most cases, you will want to delegate to the [DragAndDropModifierNode] returned by
 * the eponymous factory method.
 */
interface DragAndDropModifierNode : DelegatableNode, DragAndDropTarget {
    /**
     * Begins a drag and drop session for transferring data.
     *
     * @param transferData the data to be transferred after successful completion of the
     * drag and drop gesture.
     *
     * @param decorationSize the size of the drag decoration to be drawn.
     *
     * @param drawDragDecoration provides the visual representation of the item dragged during the
     * drag and drop gesture.
     */
    fun drag(
        transferData: DragAndDropTransferData,
        decorationSize: Size,
        drawDragDecoration: DrawScope.() -> Unit,
    )

    /**
     * The entry point to register interest in a drag and drop session for receiving data.
     *
     * @return true to indicate interest in the contents of a drag and drop session, false
     * indicates no interest. If false is returned, this [Modifier] will not receive any
     * [DragAndDropTarget] events.
     *
     * All [DragAndDropModifierNode] instances in the hierarchy will be given an opportunity
     * to participate in a drag and drop session via this method.
     */
    fun acceptDragAndDropTransfer(
        startEvent: DragAndDropEvent
    ): Boolean
}

/**
 * Creates a [Modifier.Node] for integrating with platform level drag and drop events. All
 * [DragAndDropModifierNode] instances provided by this function may start drag and drop events
 * by calling [DragAndDropModifierNode.drag].
 *
 * @param acceptDragAndDropTransfer a provider of a [DragAndDropTarget] that allows
 * this [Modifier.Node] to receive from a drag and drop gesture on a per session basis.
 *
 * If one is not provided for a given session, this [DragAndDropModifierNode] will not receive
 * [DragAndDropTarget] events for that session.
 */
fun DragAndDropModifierNode(
    acceptDragAndDropTransfer: (event: DragAndDropEvent) -> DragAndDropTarget? = { null }
): DragAndDropModifierNode = DragAndDropNode(acceptDragAndDropTransfer)

/**
 * Core implementation of drag and drop. This [Modifier.Node] implements tree traversal for
 * drag and drop, as well as hit testing and propagation of events for drag or drop gestures.
 *
 * It uses the [DragAndDropEvent] as a representation of a single mutable drag and drop session.
 */
internal class DragAndDropNode(
    private val onDragAndDropStart: (event: DragAndDropEvent) -> DragAndDropTarget?
) : Modifier.Node(),
    TraversableNode,
    DragAndDropModifierNode {
    companion object {
        private object DragAndDropTraversableKey

        private inline fun DragAndDropModifierNode.firstChildOrNull(
            crossinline predicate: (DragAndDropModifierNode) -> Boolean
        ): DragAndDropModifierNode? {
            // TODO: b/303904810 unattached nodes should not be found from an attached
            //  root drag and drop node
            if (!node.isAttached) return null
            var match: DragAndDropModifierNode? = null
            traverseDescendants(DragAndDropTraversableKey) { child ->
                if (child is DragAndDropModifierNode && predicate(child)) {
                    match = child
                    return@traverseDescendants TraverseDescendantsAction.CancelTraversal
                }
                TraverseDescendantsAction.ContinueTraversal
            }
            return match
        }
    }

    override val traverseKey: Any = DragAndDropTraversableKey

    /** Child currently receiving drag gestures for dropping into **/
    private var lastChildDragAndDropModifierNode: DragAndDropModifierNode? = null

    /** This as a drop target if eligible for processing **/
    private var thisDragAndDropTarget: DragAndDropTarget? = null

    // start Node
    override fun onDetach() {
        // Clean up
        thisDragAndDropTarget = null
        lastChildDragAndDropModifierNode = null
    }

    // end Node

    // start DragAndDropModifierNode

    override fun drag(
        transferData: DragAndDropTransferData,
        decorationSize: Size,
        drawDragDecoration: DrawScope.() -> Unit,
    ) {
        requireOwner().dragAndDropManager.drag(
            transferData = transferData,
            decorationSize = decorationSize,
            drawDragDecoration = drawDragDecoration
        )
    }

    override fun acceptDragAndDropTransfer(startEvent: DragAndDropEvent): Boolean {
        // TODO: b/303904810 unattached nodes should not be found from an attached
        //  root drag and drop node
        if (!isAttached) return false

        check(thisDragAndDropTarget == null) {
            "DragAndDropTarget self reference must be null at the start of a drag and drop session"
        }

        // Start receiving events
        thisDragAndDropTarget = onDragAndDropStart(startEvent)

        var handledByChild = false

        traverseChildren { child ->
            handledByChild = handledByChild or child.acceptDragAndDropTransfer(
                startEvent = startEvent,
            ).also { accepted ->
                if (accepted) requireOwner().dragAndDropManager.registerNodeInterest(child)
            }
            true
        }

        return handledByChild || thisDragAndDropTarget != null
    }

    // end DragAndDropModifierNode

    // start DropTarget

    override fun onStarted(event: DragAndDropEvent) {
        when (val self = thisDragAndDropTarget) {
            null -> lastChildDragAndDropModifierNode?.onStarted(event = event)
            else -> self.onStarted(event = event)
        }
    }

    override fun onEntered(event: DragAndDropEvent) {
        when (val self = thisDragAndDropTarget) {
            null -> lastChildDragAndDropModifierNode?.onEntered(event = event)
            else -> self.onEntered(event = event)
        }
    }

    override fun onMoved(event: DragAndDropEvent) {
        val currentChildNode: DragAndDropModifierNode? = lastChildDragAndDropModifierNode
        val newChildNode: DragAndDropModifierNode? = when {
            // Moved within child.
            currentChildNode?.contains(event.positionInRoot) == true -> currentChildNode
            // Position is now outside active child, maybe it entered a different one.
            else -> firstChildOrNull { child ->
                // Only dispatch to children who previously accepted the onStart gesture
                requireOwner().dragAndDropManager.isInterestedNode(child) &&
                    child.contains(event.positionInRoot)
            }
        }

        when {
            // Left us and went to a child.
            newChildNode != null && currentChildNode == null -> {
                thisDragAndDropTarget?.onExited(event = event)
                newChildNode.dispatchEntered(event)
            }
            // Left the child and returned to us.
            newChildNode == null && currentChildNode != null -> {
                currentChildNode.onExited(event = event)
                thisDragAndDropTarget?.dispatchEntered(event)
            }
            // Left one child and entered another.
            newChildNode != currentChildNode -> {
                currentChildNode?.onExited(event = event)
                newChildNode?.dispatchEntered(event)
            }
            // Stayed in the same child.
            newChildNode != null -> newChildNode.onMoved(event = event)
            // Stayed in us.
            else -> thisDragAndDropTarget?.onMoved(event = event)
        }

        this@DragAndDropNode.lastChildDragAndDropModifierNode = newChildNode
    }

    override fun onChanged(event: DragAndDropEvent) {
        when (val self = thisDragAndDropTarget) {
            null -> lastChildDragAndDropModifierNode?.onChanged(event = event)

            else -> self.onChanged(event = event)
        }
    }

    override fun onExited(event: DragAndDropEvent) {
        thisDragAndDropTarget?.onExited(event = event)
        lastChildDragAndDropModifierNode?.onExited(event = event)
        lastChildDragAndDropModifierNode = null
    }

    override fun onDropped(event: DragAndDropEvent): Boolean {
        return when (val currentChildDropTarget = lastChildDragAndDropModifierNode) {
            null -> thisDragAndDropTarget?.onDropped(event = event) ?: false

            else -> currentChildDropTarget.onDropped(event = event)
        }
    }

    override fun onEnded(event: DragAndDropEvent) {
        // TODO: b/303904810 unattached nodes should not be found from an attached
        //  root drag and drop node
        if (!node.isAttached) return
        traverseChildren { child ->
            child.onEnded(event = event)
            true
        }
        thisDragAndDropTarget?.onEnded(event = event)
        thisDragAndDropTarget = null
        lastChildDragAndDropModifierNode = null
    }
    // end DropTarget
}

private fun DragAndDropTarget.dispatchEntered(event: DragAndDropEvent) = run {
    // Notify of entry
    onEntered(event = event)
    // Start move
    onMoved(event = event)
}

/**
 * Hit test for a [DragAndDropNode].
 */
private fun DragAndDropModifierNode.contains(position: Offset): Boolean {
    if (!node.isAttached) return false
    val currentCoordinates = requireLayoutNode().coordinates
    if (!currentCoordinates.isAttached) return false

    val (width, height) = currentCoordinates.size
    val (x1, y1) = currentCoordinates.positionInRoot()
    val x2 = x1 + width
    val y2 = y1 + height

    return position.x in x1..x2 && position.y in y1..y2
}
