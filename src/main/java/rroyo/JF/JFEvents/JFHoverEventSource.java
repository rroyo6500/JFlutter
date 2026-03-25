package rroyo.JF.JFEvents;

import rroyo.JF.JFComponents.JFComponent;

/**
 * Defines the contract for components capable of firing hover events.
 */
public interface JFHoverEventSource {

    /**
     * Registers a new hover listener.
     *
     * @param listener listener to register
     */
    JFComponent addHoverListener(JFHoverListener listener);

    /**
     * Removes a previously registered hover listener.
     *
     * @param listener listener to remove
     */
    JFComponent removeHoverListener(JFHoverListener listener);

    /**
     * Dispatches a hover event to all registered listeners.
     *
     * @param event event to dispatch
     */
    JFComponent dispatchHoverEvent(JFHoverEvent event);
}
