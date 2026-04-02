package rroyo.JF.JFEvents;

import rroyo.JF.JFComponents.BaseComponent.JFComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the behavior required by components that can emit action events.
 * <p>
 * The interface provides default implementations for listener registration and event dispatch,
 * which allows any component to become clickable or pressable just by implementing this type.
 * Listener storage is delegated to {@link JFEventStore} so components themselves stay lightweight.
 *
 * @author rroyo
 */
public interface JFActionComponent {

    /**
     * Registers a new action listener on this component.
     *
     * @param listener listener to register
     * @return the current component cast as {@link JFComponent} for fluent configuration
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
     * @return the current component cast as {@link JFComponent}
     */
    default JFComponent removeActionListener(JFActionListener listener) {
        JFEventStore.actionListenersFor(this).remove(listener);
        return (JFComponent) this;
    }

    /**
     * Returns the first registered action listener for this component.
     * <p>
     * This is primarily a convenience accessor for scenarios where only one listener is expected.
     *
     * @return first registered action listener
     */
    default JFActionListener getActionListener() {
        return (JFActionListener) JFEventStore.actionListenersFor(this).getFirst();
    }

    /**
     * Dispatches an action event to all registered listeners.
     * <p>
     * A copy of the listener list is created before iteration so callbacks can safely alter
     * listener registration during dispatch without breaking the iteration.
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
