package rroyo.jf.events;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventDispatcher {

    private final Map<Class<?>, List<Object>> listeners = new HashMap<>();

    public <T> void addListener(Class<T> type, T listener) {
        listeners.computeIfAbsent(type, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    public <T> void removeListener(Class<T> type, T listener) {
        List<Object> list = listeners.get(type);
        if (list != null) {
            list.remove(listener);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getListeners(Class<T> type) {
        return (List<T>) listeners.getOrDefault(type, Collections.emptyList());
    }
}