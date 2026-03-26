package rroyo.JF.JFEvents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Centralized listener storage used by event source interfaces.
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
     * Utility class constructor hidden intentionally.
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
}
