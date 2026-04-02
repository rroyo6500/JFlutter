package rroyo.JF.JFEvents;

import rroyo.JF.JFComponents.BaseComponent.JFComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the keyboard-listener capabilities of a focusable component.
 * <p>
 * Implementing this interface allows a component to register keyboard listeners in the
 * shared event store and receive keyboard callbacks when a {@code JFWindow} routes events
 * to the component currently holding focus. The interface mirrors the rest of the event
 * system so keyboard-enabled components can be written with the same fluent style used
 * for action and hover listeners.
 *
 * @author rroyo
 */
public interface JFKeyComponent {

    /**
     * Registers a keyboard listener for this component.
     * <p>
     * The listener is stored in the shared event registry and will be invoked whenever the
     * owning window dispatches keyboard input to this component because it currently has focus.
     *
     * @param listener listener to add
     * @return the current component cast as {@link JFComponent} to support fluent APIs
     * @throws IllegalArgumentException when {@code listener} is {@code null}
     */
    default JFComponent addKeyListener(JFKeyListener listener) {
        if (listener == null) throw new IllegalArgumentException("Listener cannot be null");
        JFEventStore.keyListenersFor(this).add(listener);
        return (JFComponent) this;
    }

    /**
     * Removes a previously registered keyboard listener.
     * <p>
     * If the listener is not currently registered, the call has no effect.
     *
     * @param listener listener to remove
     * @return the current component cast as {@link JFComponent}
     */
    default JFComponent removeKeyListener(JFKeyListener listener) {
        JFEventStore.keyListenersFor(this).remove(listener);
        return (JFComponent) this;
    }

    /**
     * Dispatches a keyboard event to a snapshot of the registered listeners.
     * <p>
     * The listener list is copied before iteration so listeners can safely register or remove
     * listeners during callback execution without causing concurrent modification failures.
     * This is especially useful for components that change mode or focus in reaction to a
     * key press.
     *
     * @param event keyboard event to dispatch
     */
    default void dispatchKeyEvent(JFKeyEvent event) {
        List<JFKeyListener> listeners = new ArrayList<>(JFEventStore.keyListenersFor(this));
        for (JFKeyListener listener : listeners) {
            listener.actionPerformed(event);
        }
    }

}
