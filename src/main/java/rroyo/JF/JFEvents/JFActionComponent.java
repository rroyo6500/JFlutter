package rroyo.JF.JFEvents;

import rroyo.JF.JFComponents.JFComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the contract for components capable of firing action events.
 * Only components implementing this interface can dispatch action callbacks.
 *
 * @author rroyo
 */
public interface JFActionComponent {

    /**
     * Registers a new action listener.
     *
     * @param listener listener to register
     */
    default JFComponent addActionListener(JFActionListener listener) {
        if (listener == null) throw new IllegalArgumentException("Listener cannot be null");
        JFEventStore.actionListenersFor(this).add(listener);
        return (JFComponent) this;
    }

    /**
     * Removes a previously registered action listener.
     *
     * @param listener listener to remove
     */
    default JFComponent removeActionListener(JFActionListener listener) {
        JFEventStore.actionListenersFor(this).remove(listener);
        return (JFComponent) this;
    }

    /**
     * Dispatches an action event to all registered listeners.
     *
     * @param event event to dispatch
     */
    default void dispatchActionEvent(JFActionEvent event) {
        List<JFActionListener> listeners = new ArrayList<>(JFEventStore.actionListenersFor(this));
        for (JFActionListener listener : listeners) {
            listener.actionPerformed(event);
        }
    }
}
