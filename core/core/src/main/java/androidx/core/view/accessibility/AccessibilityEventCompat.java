/*
 * Copyright (C) 2011 The Android Open Source Project
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

package androidx.core.view.accessibility;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX;

import android.os.Build;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityRecord;
import android.widget.EditText;

import androidx.annotation.DoNotInline;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Helper for accessing features in {@link AccessibilityEvent}.
 */
public final class AccessibilityEventCompat {
    /**
     * Represents the event of a hover enter over a {@link View}.
     * @deprecated Use {@link  AccessibilityEvent#TYPE_VIEW_HOVER_ENTER} directly.
     */
    @Deprecated
    public static final int TYPE_VIEW_HOVER_ENTER = AccessibilityEvent.TYPE_VIEW_HOVER_ENTER;

    /**
     * Represents the event of a hover exit over a {@link View}.
     * @deprecated Use {@link  AccessibilityEvent#TYPE_VIEW_HOVER_EXIT} directly.
     */
    @Deprecated
    public static final int TYPE_VIEW_HOVER_EXIT = AccessibilityEvent.TYPE_VIEW_HOVER_EXIT;

    /**
     * Represents the event of starting a touch exploration gesture.
     * @deprecated Use {@link  AccessibilityEvent#TYPE_TOUCH_EXPLORATION_GESTURE_START} directly.
     */
    @Deprecated
    public static final int TYPE_TOUCH_EXPLORATION_GESTURE_START =
            AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START;

    /**
     * Represents the event of ending a touch exploration gesture.
     * @deprecated Use {@link AccessibilityEvent#TYPE_TOUCH_EXPLORATION_GESTURE_END} directly.
     */
    @Deprecated
    public static final int TYPE_TOUCH_EXPLORATION_GESTURE_END =
            AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END;

    /**
     * Represents the event of changing the content of a window.
     * @deprecated Use {@link AccessibilityEvent#TYPE_WINDOW_CONTENT_CHANGED} directly.
     */
    @Deprecated
    public static final int TYPE_WINDOW_CONTENT_CHANGED =
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;

    /**
     * Represents the event of scrolling a view.
     * @deprecated Use {@link AccessibilityEvent#TYPE_VIEW_SCROLLED} directly.
     */
    @Deprecated
    public static final int TYPE_VIEW_SCROLLED = AccessibilityEvent.TYPE_VIEW_SCROLLED;

    /**
     * Represents the event of changing the selection in an {@link EditText}.
     * @deprecated Use {@link AccessibilityEvent#TYPE_VIEW_TEXT_SELECTION_CHANGED} directly.
     */
    @Deprecated
    public static final int TYPE_VIEW_TEXT_SELECTION_CHANGED =
            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED;

    /**
     * Represents the event of an application making an announcement.
     *
     * <p>
     * Note: This event carries no semantic meaning and is appropriate only in exceptional
     * situations. Apps can generally achieve correct behavior for accessibility by
     * accurately supplying the semantics of their UI. They should not need to specify what
     * exactly is announced to users.
     *
     * <p>
     * In general, only announce transitions that can't be handled by the following preferred
     * alternatives:
     * <ul>
     *     <li>Label your controls concisely and precisely. Don’t generate a confirmation message
     *     for simple actions like a button press.</li>
     *     <li>For significant UI changes like window changes, use
     *     {@link android.app.Activity#setTitle(CharSequence)} and
     *     {@link androidx.core.view.ViewCompat#setAccessibilityPaneTitle(View, CharSequence)} )}.
     *     </li>
     *     <li>Use {@link androidx.core.view.ViewCompat#setAccessibilityLiveRegion(View, int)}
     *     to inform the user of changes to critical views within the user interface. These
     *     should still be used sparingly as they may generate announcements every time a View is
     *     updated.</li>
     * </ul>
     */
    public static final int TYPE_ANNOUNCEMENT = 0x00004000;

    /**
     * Represents the event of gaining accessibility focus.
     * @see AccessibilityNodeInfoCompat.AccessibilityActionCompat#ACTION_ACCESSIBILITY_FOCUS
     */
    public static final int TYPE_VIEW_ACCESSIBILITY_FOCUSED = 0x00008000;

    /**
     * Represents the event of clearing accessibility focus.
     * @see AccessibilityNodeInfoCompat.AccessibilityActionCompat#ACTION_CLEAR_ACCESSIBILITY_FOCUS
     */
    public static final int TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED = 0x00010000;

    /**
     * Represents the event of traversing the text of a view at a given movement granularity.
     */
    public static final int TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY = 0x00020000;

    /**
     * Represents the event of beginning gesture detection.
     */
    public static final int TYPE_GESTURE_DETECTION_START = 0x00040000;

    /**
     * Represents the event of ending gesture detection.
     */
    public static final int TYPE_GESTURE_DETECTION_END = 0x00080000;

    /**
     * Represents the event of the user starting to touch the screen.
     */
    public static final int TYPE_TOUCH_INTERACTION_START = 0x00100000;

    /**
     * Represents the event of the user ending to touch the screen.
     */
    public static final int TYPE_TOUCH_INTERACTION_END = 0x00200000;

    /**
     * Represents the event change in the windows shown on the screen.
     */
    public static final int TYPE_WINDOWS_CHANGED = 0x00400000;

    /**
     * Represents the event of a context click on a {@link View}.
     */
    public static final int TYPE_VIEW_CONTEXT_CLICKED = 0x00800000;

    /**
     * Represents the event of the assistant currently reading the users screen context.
     */
    public static final int TYPE_ASSIST_READING_CONTEXT = 0x01000000;

    /**
     * Represents the event of a scroll having completed and brought the target node on screen.
     */
    public static final int TYPE_VIEW_TARGETED_BY_SCROLL = 0x04000000;

    /**
     * Change type for {@link #TYPE_WINDOW_CONTENT_CHANGED} event:
     * The type of change is not defined.
     */
    public static final int CONTENT_CHANGE_TYPE_UNDEFINED = 0x00000000;

    /**
     * Change type for {@link #TYPE_WINDOW_CONTENT_CHANGED} event:
     * A node in the subtree rooted at the source node was added or removed.
     */
    public static final int CONTENT_CHANGE_TYPE_SUBTREE = 0x00000001;

    /**
     * Change type for {@link #TYPE_WINDOW_CONTENT_CHANGED} event:
     * The node's text changed.
     */
    public static final int CONTENT_CHANGE_TYPE_TEXT = 1 << 1;

    /**
     * Change type for {@link #TYPE_WINDOW_CONTENT_CHANGED} event:
     * The node's content description changed.
     */
    public static final int CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION = 1 << 2;

    /**
     * Change type for {@link AccessibilityEvent#TYPE_WINDOW_STATE_CHANGED} event:
     * The node's pane title changed.
     * @see androidx.core.view.ViewCompat#setAccessibilityPaneTitle(View, CharSequence)
     */
    public static final int CONTENT_CHANGE_TYPE_PANE_TITLE = 1 << 3;

    /**
     * Change type for {@link AccessibilityEvent#TYPE_WINDOW_STATE_CHANGED} event:
     * The node has a pane title, and either just appeared or just was assigned a title when it
     * had none before.
     * @see androidx.core.view.ViewCompat#setAccessibilityPaneTitle(View, CharSequence)
     */
    public static final int CONTENT_CHANGE_TYPE_PANE_APPEARED = 1 << 4;

    /**
     * Change type for {@link AccessibilityEvent#TYPE_WINDOW_STATE_CHANGED} event:
     * Can mean one of two slightly different things. The primary meaning is that the node has
     * a pane title, and was removed from the node hierarchy. It can also be sent if the pane
     * title is set to {@code null} after it contained a title.
     * No source will be returned if the node is no longer on the screen. To make the change more
     * clear for the user, the first entry in {@link AccessibilityRecord#getText()} can return the
     * value that would have been returned by {@code getSource().getPaneTitle()}.
     * @see androidx.core.view.ViewCompat#setAccessibilityPaneTitle(View, CharSequence)
     */
    public static final int CONTENT_CHANGE_TYPE_PANE_DISAPPEARED = 1 << 5;

    /**
     * Change type for {@link AccessibilityEvent#TYPE_WINDOW_CONTENT_CHANGED} event:
     * state description of the node as returned by
     * {@link AccessibilityNodeInfoCompat#getStateDescription} was changed.
     */
    public static final int CONTENT_CHANGE_TYPE_STATE_DESCRIPTION = 1 << 6;

    /**
     * Change type for {@link #TYPE_WINDOW_CONTENT_CHANGED} event:
     * A drag has started while accessibility is enabled. This is either via an
     * AccessibilityAction, or via touch events. This is sent from the source that initiated the
     * drag.
     *
     * @see AccessibilityNodeInfoCompat.AccessibilityActionCompat#ACTION_DRAG_START
     */
    public static final int CONTENT_CHANGE_TYPE_DRAG_STARTED = 1 << 7;

    /**
     * Change type for {@link #TYPE_WINDOW_CONTENT_CHANGED} event:
     * A drag in with accessibility enabled has ended. This means the content has been
     * successfully dropped. This is sent from the target that accepted the dragged content.
     *
     * @see AccessibilityNodeInfoCompat.AccessibilityActionCompat#ACTION_DRAG_DROP
     */
    public static final int CONTENT_CHANGE_TYPE_DRAG_DROPPED = 1 << 8;

    /**
     * Change type for {@link #TYPE_WINDOW_CONTENT_CHANGED} event:
     * A drag in with accessibility enabled has ended. This means the content has been
     * unsuccessfully dropped, the user has canceled the action via an AccessibilityAction, or
     * no drop has been detected within a timeout and the drag was automatically cancelled. This is
     * sent from the source that initiated the drag.
     *
     * @see AccessibilityNodeInfoCompat.AccessibilityActionCompat#ACTION_DRAG_CANCEL
     */
    public static final int CONTENT_CHANGE_TYPE_DRAG_CANCELLED = 1 << 9;

    /**
     * Change type for {@link #TYPE_WINDOW_CONTENT_CHANGED} event:
     * The source node changed its content validity returned by
     * {@link AccessibilityNodeInfoCompat#isContentInvalid()}.
     * The view changing content validity should call
     * {@link AccessibilityNodeInfoCompat#setContentInvalid(boolean)} and then send this event.
     *
     * @see AccessibilityNodeInfoCompat#isContentInvalid()
     * @see AccessibilityNodeInfoCompat#setContentInvalid(boolean)
     */
    public static final int CONTENT_CHANGE_TYPE_CONTENT_INVALID = 1 << 10;

    /**
     * Change type for {@link #TYPE_WINDOW_CONTENT_CHANGED} event:
     * The source node changed its erroneous content's error message returned by
     * {@link AccessibilityNodeInfoCompat#getError()}.
     * The view changing erroneous content's error message should call
     * {@link AccessibilityNodeInfoCompat#setError(CharSequence)} and then send this event.
     *
     * @see AccessibilityNodeInfoCompat#getError()
     * @see AccessibilityNodeInfoCompat#setError(CharSequence)
     */
    public static final int CONTENT_CHANGE_TYPE_ERROR = 1 << 11;


    /**
     * Change type for {@link #TYPE_WINDOW_CONTENT_CHANGED} event:
     * The source node changed its ability to interact returned by
     * {@link AccessibilityNodeInfoCompat#isEnabled()}.
     * The view changing content's ability to interact should call
     * {@link AccessibilityNodeInfoCompat#setEnabled(boolean)} and then send this event.
     *
     * @see AccessibilityNodeInfoCompat#isEnabled()
     * @see AccessibilityNodeInfoCompat#setEnabled(boolean)
     */
    public static final int CONTENT_CHANGE_TYPE_ENABLED = 1 << 12;

    /**
     * Mask for {@link AccessibilityEvent} all types.
     *
     * @see AccessibilityEvent#TYPE_VIEW_CLICKED
     * @see AccessibilityEvent#TYPE_VIEW_LONG_CLICKED
     * @see AccessibilityEvent#TYPE_VIEW_SELECTED
     * @see AccessibilityEvent#TYPE_VIEW_FOCUSED
     * @see AccessibilityEvent#TYPE_VIEW_TEXT_CHANGED
     * @see AccessibilityEvent#TYPE_WINDOW_STATE_CHANGED
     * @see AccessibilityEvent#TYPE_NOTIFICATION_STATE_CHANGED
     * @see AccessibilityEvent#TYPE_VIEW_HOVER_ENTER
     * @see AccessibilityEvent#TYPE_VIEW_HOVER_EXIT
     * @see AccessibilityEvent#TYPE_TOUCH_EXPLORATION_GESTURE_START
     * @see AccessibilityEvent#TYPE_TOUCH_EXPLORATION_GESTURE_END
     * @see AccessibilityEvent#TYPE_WINDOW_CONTENT_CHANGED
     * @see AccessibilityEvent#TYPE_VIEW_SCROLLED
     * @see AccessibilityEvent#TYPE_VIEW_TEXT_SELECTION_CHANGED
     * @see AccessibilityEvent#TYPE_VIEW_TARGETED_BY_SCROLL
     * @see #TYPE_ANNOUNCEMENT
     * @see #TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY
     * @see #TYPE_GESTURE_DETECTION_START
     * @see #TYPE_GESTURE_DETECTION_END
     * @see #TYPE_TOUCH_INTERACTION_START
     * @see #TYPE_TOUCH_INTERACTION_END
     * @see #TYPE_WINDOWS_CHANGED
     * @see #TYPE_VIEW_CONTEXT_CLICKED
     * @see #TYPE_ASSIST_READING_CONTEXT
     */
    public static final int TYPES_ALL_MASK = 0xFFFFFFFF;

    @IntDef(
            flag = true,
            value = {
                    CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION,
                    CONTENT_CHANGE_TYPE_STATE_DESCRIPTION,
                    CONTENT_CHANGE_TYPE_SUBTREE,
                    CONTENT_CHANGE_TYPE_TEXT,
                    CONTENT_CHANGE_TYPE_UNDEFINED,
                    CONTENT_CHANGE_TYPE_DRAG_STARTED,
                    CONTENT_CHANGE_TYPE_DRAG_DROPPED,
                    CONTENT_CHANGE_TYPE_DRAG_CANCELLED,
                    CONTENT_CHANGE_TYPE_CONTENT_INVALID,
                    CONTENT_CHANGE_TYPE_ERROR,
                    CONTENT_CHANGE_TYPE_ENABLED
            })
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    @Retention(RetentionPolicy.SOURCE)
    public @interface ContentChangeType {}

    /*
     * Hide constructor from clients.
     */
    private AccessibilityEventCompat() {

    }

    /**
     * Gets the number of records contained in the event.
     *
     * @return The number of records.
     *
     * @deprecated Use {@link AccessibilityEvent#getRecordCount()} directly.
     */
    @Deprecated
    public static int getRecordCount(AccessibilityEvent event) {
        return event.getRecordCount();
    }

    /**
     * Appends an {@link AccessibilityRecord} to the end of
     * event records.
     *
     * @param event event for which to append the record.
     * @param record The record to append.
     *
     * @throws IllegalStateException If called from an AccessibilityService.
     *
     * @deprecated Use {@link AccessibilityEvent#appendRecord(AccessibilityRecord)} directly.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public static void appendRecord(AccessibilityEvent event, AccessibilityRecordCompat record) {
        event.appendRecord((AccessibilityRecord) record.getImpl());
    }

    /**
     * Gets the record at a given index.
     *
     * @param event event for which to get the record.
     * @param index The index.
     * @return The record at the specified index.
     *
     * @deprecated Use {@link AccessibilityEvent#getRecord(int)} directly.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public static AccessibilityRecordCompat getRecord(AccessibilityEvent event, int index) {
        return new AccessibilityRecordCompat(event.getRecord(index));
    }

    /**
     * Creates an {@link AccessibilityRecordCompat} from an {@link AccessibilityEvent}
     * that can be used to manipulate the event properties defined in
     * {@link AccessibilityRecord}.
     * <p>
     * <strong>Note:</strong> Do not call {@link AccessibilityRecordCompat#recycle()} on the
     * returned {@link AccessibilityRecordCompat}. Call {@link AccessibilityEvent#recycle()}
     * in case you want to recycle the event.
     * </p>
     *
     * @param event The from which to create a record.
     * @return An {@link AccessibilityRecordCompat}.
     *
     * @deprecated Use the {@link AccessibilityEvent} directly as {@link AccessibilityRecord}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public static AccessibilityRecordCompat asRecord(AccessibilityEvent event) {
        return new AccessibilityRecordCompat(event);
    }

    /**
     * Sets the bit mask of node tree changes signaled by an
     * {@link #TYPE_WINDOW_CONTENT_CHANGED} event.
     *
     * @param event event for which to set the types.
     * @param changeTypes The bit mask of change types.
     * @throws IllegalStateException If called from an AccessibilityService.
     * @see #getContentChangeTypes(AccessibilityEvent)
     */
    public static void setContentChangeTypes(@NonNull AccessibilityEvent event,
            @ContentChangeType int changeTypes) {
        if (Build.VERSION.SDK_INT >= 19) {
            Api19Impl.setContentChangeTypes(event, changeTypes);
        }
    }

    /**
     * Gets the bit mask of change types signaled by an
     * {@link #TYPE_WINDOW_CONTENT_CHANGED} event. A single event may represent
     * multiple change types.
     *
     * @return The bit mask of change types. One or more of:
     *         <ul>
     *         <li>{@link AccessibilityEvent#CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION}
     *         <li>{@link AccessibilityEvent#CONTENT_CHANGE_TYPE_STATE_DESCRIPTION}
     *         <li>{@link AccessibilityEvent#CONTENT_CHANGE_TYPE_SUBTREE}
     *         <li>{@link AccessibilityEvent#CONTENT_CHANGE_TYPE_TEXT}
     *         <li>{@link AccessibilityEvent#CONTENT_CHANGE_TYPE_UNDEFINED}
     *         </ul>
     */
    @ContentChangeType
    public static int getContentChangeTypes(@NonNull AccessibilityEvent event) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Api19Impl.getContentChangeTypes(event);
        } else {
            return 0;
        }
    }

    /**
     * Sets the movement granularity that was traversed.
     *
     * @param event event for which to set the granularity.
     * @param granularity The granularity.
     *
     * @throws IllegalStateException If called from an AccessibilityService.
     */
    public static void setMovementGranularity(@NonNull AccessibilityEvent event, int granularity) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.setMovementGranularity(event, granularity);
        }
    }

    /**
     * Gets the movement granularity that was traversed.
     *
     * @return The granularity.
     */
    public static int getMovementGranularity(@NonNull AccessibilityEvent event) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.getMovementGranularity(event);
        } else {
            return 0;
        }
    }

    /**
     * Sets the performed action that triggered this event.
     * <p>
     * Valid actions are defined in {@link AccessibilityNodeInfoCompat}:
     * <ul>
     * <li>{@link AccessibilityNodeInfoCompat#ACTION_ACCESSIBILITY_FOCUS}
     * <li>{@link AccessibilityNodeInfoCompat#ACTION_CLEAR_ACCESSIBILITY_FOCUS}
     * <li>{@link AccessibilityNodeInfoCompat#ACTION_CLEAR_FOCUS}
     * <li>{@link AccessibilityNodeInfoCompat#ACTION_CLEAR_SELECTION}
     * <li>{@link AccessibilityNodeInfoCompat#ACTION_CLICK}
     * <li>etc.
     * </ul>
     *
     * @param event event for which to set the action.
     * @param action The action.
     * @throws IllegalStateException If called from an AccessibilityService.
     * @see AccessibilityNodeInfoCompat#performAction(int)
     */
    public static void setAction(@NonNull AccessibilityEvent event, int action) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.setAction(event, action);
        }
    }

    /**
     * Gets the performed action that triggered this event.
     *
     * @return The action.
     */
    public static int getAction(@NonNull AccessibilityEvent event) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.getAction(event);
        } else {
            return 0;
        }
    }

    /**
     * Whether the event should only be delivered to an
     * {@link android.accessibilityservice.AccessibilityService} with the
     * {@link android.accessibilityservice.AccessibilityServiceInfo#isAccessibilityTool} property
     * set to true.
     *
     * <p>
     * Initial value matches the {@link android.view.View#isAccessibilityDataSensitive} property
     * from the event's source node, if present, or false by default.
     * </p>
     *
     * @return True if the event should be delivered only to isAccessibilityTool services, false
     * otherwise.
     * @see #setAccessibilityDataSensitive
     */
    public static boolean isAccessibilityDataSensitive(@NonNull AccessibilityEvent event) {
        if (Build.VERSION.SDK_INT >= 34) {
            return Api34Impl.isAccessibilityDataSensitive(event);
        } else {
            // To preserve behavior, assume the event's accessibility data is not sensitive.
            return false;
        }
    }

    /**
     * Sets whether the event should only be delivered to an
     * {@link android.accessibilityservice.AccessibilityService} with the
     * {@link android.accessibilityservice.AccessibilityServiceInfo#isAccessibilityTool} property
     * set to true.
     *
     * <p>
     * This will be set automatically based on the event's source (if present). If creating and
     * sending an event directly through {@link android.view.accessibility.AccessibilityManager}
     * (where an event may have no source) then this method must be called explicitly if you want
     * non-default behavior.
     * </p>
     *
     * @param event The event to update.
     * @param accessibilityDataSensitive True if the event should be delivered only to
     *                                   isAccessibilityTool services, false otherwise.
     * @throws IllegalStateException If called from an AccessibilityService.
     */
    public static void setAccessibilityDataSensitive(@NonNull AccessibilityEvent event,
            boolean accessibilityDataSensitive) {
        if (Build.VERSION.SDK_INT >= 34) {
            Api34Impl.setAccessibilityDataSensitive(event, accessibilityDataSensitive);
        }
    }

    @RequiresApi(34)
    static class Api34Impl {
        private Api34Impl() {
            // This class is not instantiable.
        }

        @DoNotInline
        static boolean isAccessibilityDataSensitive(AccessibilityEvent event) {
            return event.isAccessibilityDataSensitive();
        }

        @DoNotInline
        static void setAccessibilityDataSensitive(AccessibilityEvent event,
                boolean accessibilityDataSensitive) {
            event.setAccessibilityDataSensitive(accessibilityDataSensitive);
        }
    }
    @RequiresApi(19)
    static class Api19Impl {
        private Api19Impl() {
            // This class is not instantiable.
        }

        @DoNotInline
        static void setContentChangeTypes(AccessibilityEvent accessibilityEvent, int changeTypes) {
            accessibilityEvent.setContentChangeTypes(changeTypes);
        }

        @DoNotInline
        static int getContentChangeTypes(AccessibilityEvent accessibilityEvent) {
            return accessibilityEvent.getContentChangeTypes();
        }
    }

    @RequiresApi(16)
    static class Api16Impl {
        private Api16Impl() {
            // This class is not instantiable.
        }

        @DoNotInline
        static void setMovementGranularity(AccessibilityEvent accessibilityEvent, int granularity) {
            accessibilityEvent.setMovementGranularity(granularity);
        }

        @DoNotInline
        static int getMovementGranularity(AccessibilityEvent accessibilityEvent) {
            return accessibilityEvent.getMovementGranularity();
        }

        @DoNotInline
        static void setAction(AccessibilityEvent accessibilityEvent, int action) {
            accessibilityEvent.setAction(action);
        }

        @DoNotInline
        static int getAction(AccessibilityEvent accessibilityEvent) {
            return accessibilityEvent.getAction();
        }
    }
}
