package rroyo.JF.JFEvents;

import rroyo.JF.JFComponents.JFComponent;

/**
 * Defines the contract for components capable of firing action events.
 * Only components implementing this interface can dispatch action callbacks.
 */
public interface JFActionEventSource {

    /**
     * Registers a new action listener.
     *
     * @param listener listener to register
     */
    JFComponent addActionListener(JFActionListener listener);

    /**
     * Removes a previously registered action listener.
     *
     * @param listener listener to remove
     */
    JFComponent removeActionListener(JFActionListener listener);

    /**
     * Dispatches an action event to all registered listeners.
     *
     * @param event event to dispatch
     */
    JFComponent dispatchActionEvent(JFActionEvent event);
}
