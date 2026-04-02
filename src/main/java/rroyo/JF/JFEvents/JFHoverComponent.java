package rroyo.JF.JFEvents;

import rroyo.JF.JFComponents.BaseComponent.JFComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the hover-event capabilities of a component.
 * <p>
 * Any component implementing this interface can register listeners interested in cursor
 * transitions and movement over its visual area. The default methods keep listener management
 * consistent with the rest of the framework event system.
 *
 * @author rroyo
 */
public interface JFHoverComponent {

    /**
     * Registers a new hover listener.
     *
     * @param listener listener to register
     * @return the current component cast as {@link JFComponent} for fluent configuration
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
     * @return the current component cast as {@link JFComponent}
     */
    default JFComponent removeHoverListener(JFHoverListener listener) {
        JFEventStore.hoverListenersFor(this).remove(listener);
        return (JFComponent) this;
    }

    /**
     * Dispatches a hover event to all registered listeners.
     * <p>
     * The listener list is copied before iteration so a listener may safely change subscriptions
     * while handling the current event.
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
