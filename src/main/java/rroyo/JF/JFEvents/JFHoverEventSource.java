package rroyo.JF.JFEvents;

import rroyo.JF.JFComponents.JFComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the contract for components capable of firing hover events.
 */
public interface JFHoverEventSource {

    /**
     * Registers a new hover listener.
     *
     * @param listener listener to register
     */
    default JFComponent addHoverListener(JFHoverListener listener) {
        if (listener == null) throw new IllegalArgumentException("Listener cannot be null");
        JFEventStore.hoverListenersFor(this).add(listener);
        return (JFComponent) this;
    }

    /**
     * Removes a previously registered hover listener.
     *
     * @param listener listener to remove
     */
    default JFComponent removeHoverListener(JFHoverListener listener) {
        JFEventStore.hoverListenersFor(this).remove(listener);
        return (JFComponent) this;
    }

    /**
     * Dispatches a hover event to all registered listeners.
     *
     * @param event event to dispatch
     */
    default JFComponent dispatchHoverEvent(JFHoverEvent event) {
        List<JFHoverListener> listeners = new ArrayList<>(JFEventStore.hoverListenersFor(this));
        for (JFHoverListener listener : listeners) {
            listener.hoverEvent(event);
        }
        return (JFComponent) this;
    }
}
