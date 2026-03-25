package rroyo.JF.JFEvents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Centralized listener storage used by event source interfaces.
 */
final class JFEventStore {

    static final Map<JFActionEventSource, List<JFActionListener>> ACTION_LISTENERS =
            Collections.synchronizedMap(new WeakHashMap<>());

    static final Map<JFHoverEventSource, List<JFHoverListener>> HOVER_LISTENERS =
            Collections.synchronizedMap(new WeakHashMap<>());

    private JFEventStore() {
    }

    static List<JFActionListener> actionListenersFor(JFActionEventSource source) {
        return ACTION_LISTENERS.computeIfAbsent(source, ignored -> new ArrayList<>());
    }

    static List<JFHoverListener> hoverListenersFor(JFHoverEventSource source) {
        return HOVER_LISTENERS.computeIfAbsent(source, ignored -> new ArrayList<>());
    }
}
