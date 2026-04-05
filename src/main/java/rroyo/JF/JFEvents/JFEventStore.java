package rroyo.JF.JFEvents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Centralized listener registry used by the framework event interfaces.
 * <p>
 * Instead of forcing every component implementation to manage its own listener collections,
 * the event-capable interfaces delegate that responsibility to this package-private utility.
 * Weak keys are used so listener maps do not keep components alive after they are no longer
 * referenced elsewhere in the component tree.
 *
 * @author rroyo
 */
final class JFEventStore {

    /**
     * Listener registry for action event sources.
     */
    static final Map<JFActionComponent, List<JFActionListener>> ACTION_LISTENERS =
            Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * Listener registry for hover event sources.
     */
    static final Map<JFHoverComponent, List<JFHoverListener>> HOVER_LISTENERS =
            Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * Listener registry for key event sources.
     */
    static final Map<JFKeyComponent, List<JFKeyListener>> KEY_LISTENERS =
            Collections.synchronizedMap(new WeakHashMap<>());
    /**
     * Listener registry for wheel event sources.
     */
    static final Map<JFWheelComponent, List<JFWheelListener>> WHEEL_LISTENERS =
            Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * Hidden constructor because this type is only a static utility holder.
     */
    private JFEventStore() {
    }

    /**
     * Returns the action listener list for a source, creating it when absent.
     *
     * @param source action event source key
     * @return mutable listener list bound to the source
     */
    static List<JFActionListener> actionListenersFor(JFActionComponent source) {
        return ACTION_LISTENERS.computeIfAbsent(source, ignored -> new ArrayList<>());
    }

    /**
     * Returns the hover listener list for a source, creating it when absent.
     *
     * @param source hover event source key
     * @return mutable listener list bound to the source
     */
    static List<JFHoverListener> hoverListenersFor(JFHoverComponent source) {
        return HOVER_LISTENERS.computeIfAbsent(source, ignored -> new ArrayList<>());
    }

    /**
     * Returns the keyboard listener list for a source, creating it when absent.
     *
     * @param source key event source key
     * @return mutable listener list bound to the source
     */
    static List<JFKeyListener> keyListenersFor(JFKeyComponent source) {
        return KEY_LISTENERS.computeIfAbsent(source, ignored -> new ArrayList<>());
    }

    /**
     * Returns the wheel listener list for a source, creating it when absent.
     *
     * @param source wheel event source key
     * @return mutable listener list bound to the source
     */
    static List<JFWheelListener> wheelListenersFor(JFWheelComponent source) {
        return WHEEL_LISTENERS.computeIfAbsent(source, ignored -> new ArrayList<>());
    }
}
