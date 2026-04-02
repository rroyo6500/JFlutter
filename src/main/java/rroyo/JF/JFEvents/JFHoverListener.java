package rroyo.JF.JFEvents;

/**
 * Functional listener interface for pointer hover notifications.
 * <p>
 * Components implementing {@link JFHoverComponent} use this contract to notify interested
 * code when the cursor enters, moves inside, or exits their visual bounds.
 *
 * @author rroyo
 */
public interface JFHoverListener {

    /**
     * Called whenever a hover transition or movement is dispatched for a component.
     *
     * @param event hover event data, including source component, pointer position and transition type
     */
    void hoverEvent(JFHoverEvent event);
}
