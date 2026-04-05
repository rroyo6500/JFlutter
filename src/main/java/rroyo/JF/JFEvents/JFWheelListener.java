package rroyo.JF.JFEvents;

/**
 * Functional listener interface for mouse-wheel notifications.
 *
 * @author rroyo
 */
public interface JFWheelListener {

    /**
     * Handles a wheel event dispatched by the framework.
     *
     * @param event wheel event wrapper containing source, coordinates and scroll delta
     */
    void wheelMoved(JFWheelEvent event);
}
