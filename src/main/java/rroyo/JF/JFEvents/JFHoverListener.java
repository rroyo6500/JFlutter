package rroyo.JF.JFEvents;

/**
 * Receives hover events for components implementing {@link JFHoverEventSource}.
 */
public interface JFHoverListener {

    /**
     * Called whenever a hover event occurs (enter, move or exit).
     *
     * @param event hover event data
     */
    void hoverEvent(JFHoverEvent event);
}
