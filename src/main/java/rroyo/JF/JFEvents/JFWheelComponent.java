package rroyo.JF.JFEvents;

import rroyo.JF.JFComponents.BaseComponent.JFComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the wheel-event capabilities of a component.
 *
 * @author rroyo
 */
public interface JFWheelComponent {

    /**
     * Registers a wheel listener.
     *
     * @param listener listener to register
     * @return current component for fluent configuration
     */
    default JFComponent addWheelListener(JFWheelListener listener) {
        if (listener == null) throw new IllegalArgumentException("Listener cannot be null");
        JFEventStore.wheelListenersFor(this).add(listener);
        return (JFComponent) this;
    }

    /**
     * Removes a previously registered wheel listener.
     *
     * @param listener listener to remove
     * @return current component for fluent configuration
     */
    default JFComponent removeWheelListener(JFWheelListener listener) {
        JFEventStore.wheelListenersFor(this).remove(listener);
        return (JFComponent) this;
    }

    /**
     * Dispatches a wheel event to a snapshot of the current listeners.
     *
     * @param event wheel event to dispatch
     */
    default void dispatchWheelEvent(JFWheelEvent event) {
        List<JFWheelListener> listeners = new ArrayList<>(JFEventStore.wheelListenersFor(this));
        for (JFWheelListener listener : listeners) {
            listener.wheelMoved(event);
        }
    }
}
