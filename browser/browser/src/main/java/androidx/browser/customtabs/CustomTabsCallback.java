/*
 * Copyright 2018 The Android Open Source Project
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

package androidx.browser.customtabs;

import static androidx.annotation.Dimension.PX;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.browser.customtabs.CustomTabsService.Relation;

/**
 * A callback class for custom tabs client to get messages regarding events in their custom tabs. In
 * the implementation, all callbacks are sent to the UI thread for the client.
 */
public class CustomTabsCallback {
    /**
     * Sent when the tab has started loading a page.
     */
    public static final int NAVIGATION_STARTED = 1;

    /**
     * Sent when the tab has finished loading a page.
     */
    public static final int NAVIGATION_FINISHED = 2;

    /**
     * Sent when the tab couldn't finish loading due to a failure.
     */
    public static final int NAVIGATION_FAILED = 3;

    /**
     * Sent when loading was aborted by a user action before it finishes like clicking on a link
     * or refreshing the page.
     */
    public static final int NAVIGATION_ABORTED = 4;

    /**
     * Sent when the tab becomes visible.
     */
    public static final int TAB_SHOWN = 5;

    /**
     * Sent when the tab becomes hidden.
     */
    public static final int TAB_HIDDEN = 6;

    /**
     * Key for the extra included in {@link #onRelationshipValidationResult} {@code extras}
     * containing whether the verification was performed while the device was online. This may be
     * missing in cases verification was short cut.
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public static final String ONLINE_EXTRAS_KEY = "online";

    /**
     * To be called when a navigation event happens.
     *
     * @param navigationEvent The code corresponding to the navigation event.
     * @param extras Reserved for future use.
     */
    public void onNavigationEvent(int navigationEvent, @Nullable Bundle extras) {}

    /**
     * Unsupported callbacks that may be provided by the implementation.
     *
     * <p>
     * <strong>Note:</strong>Clients should <strong>never</strong> rely on this callback to be
     * called and/or to have a defined behavior, as it is entirely implementation-defined and not
     * supported.
     *
     * <p> This can be used by implementations to add extra callbacks, for testing or experimental
     * purposes.
     *
     * @param callbackName Name of the extra callback.
     * @param args Arguments for the callback
     */
    public void extraCallback(@NonNull String callbackName, @Nullable Bundle args) {}

    /**
     * The same as {@link #extraCallback}, except that this method allows the custom tabs provider
     * to return a result.
     *
     * A return value of {@code null} will be used to signify that the client does not know how to
     * handle the callback.
     *
     * As optional best practices, {@link CustomTabsService#KEY_SUCCESS} could be use to identify
     * that callback was *successfully* handled. For example, when returning a message with result:
     * <pre><code>
     *     Bundle result = new Bundle();
     *     result.putString("message", message);
     *     if (success)
     *         result.putBoolean(CustomTabsService#KEY_SUCCESS, true);
     *     return result;
     * </code></pre>
     * The caller side:
     * <pre><code>
     *     Bundle result = extraCallbackWithResult(callbackName, args);
     *     if (result.getBoolean(CustomTabsService#KEY_SUCCESS)) {
     *         // callback was successfully handled
     *     }
     * </code></pre>
     */
    @Nullable
    public Bundle extraCallbackWithResult(@NonNull String callbackName, @Nullable Bundle args) {
        return null;
    }

    /**
     * Called when {@link CustomTabsSession} has requested a postMessage channel through
     * {@link CustomTabsService#requestPostMessageChannel(
     * CustomTabsSessionToken, android.net.Uri)} and the channel
     * is ready for sending and receiving messages on both ends.
     *
     * @param extras Reserved for future use.
     */
    public void onMessageChannelReady(@Nullable Bundle extras) {}

    /**
     * Called when a tab controlled by this {@link CustomTabsSession} has sent a postMessage.
     * If postMessage() is called from a single thread, then the messages will be posted in the
     * same order. When received on the client side, it is the client's responsibility to preserve
     * the ordering further.
     *
     * @param message The message sent.
     * @param extras Reserved for future use.
     */
    public void onPostMessage(@NonNull String message, @Nullable Bundle extras) {}

    /**
     * Called when a relationship validation result is available.
     *
     * @param relation Relation for which the result is available. Value previously passed to
     *                 {@link CustomTabsSession#validateRelationship(int, Uri, Bundle)}. Must be one
     *                 of the {@code CustomTabsService#RELATION_* } constants.
     * @param requestedOrigin Origin requested. Value previously passed to
     *                        {@link CustomTabsSession#validateRelationship(int, Uri, Bundle)}.
     * @param result Whether the relation was validated.
     * @param extras Reserved for future use.
     */
    public void onRelationshipValidationResult(@Relation int relation, @NonNull Uri requestedOrigin,
            boolean result, @Nullable Bundle extras) {}

    /**
     * Called when the tab is resized.
     *
     * @param height The updated height in px.
     * @param width The updated width in px.
     * @param extras Reserved for future use.
     */
    public void onActivityResized(@Dimension(unit = PX) int height,
            @Dimension(unit = PX) int width, @NonNull Bundle extras) {}

    /**
     * Called when the browser process finished warming up initiated by
     * {@link CustomTabsClient#warmup()}.
     * @param extras Reserved for future use.
     */
    public void onWarmupCompleted(@NonNull Bundle extras) {}
}
