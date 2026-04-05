package rroyo.JF.JFEvents;

import rroyo.JF.JFComponents.BaseComponent.JFComponent;

import java.awt.event.MouseWheelEvent;

/**
 * Wraps a raw AWT mouse-wheel event together with framework metadata.
 *
 * @author rroyo
 */
public class JFWheelEvent {

    private final JFComponent source;
    private final int mouseX;
    private final int mouseY;
    private final int wheelRotation;
    private final double preciseWheelRotation;
    private final MouseWheelEvent mainEvent;
    private final long timestamp;

    /**
     * Creates a new wheel-event wrapper.
     *
     * @param source component receiving the event
     * @param mouseX pointer x-coordinate in window space
     * @param mouseY pointer y-coordinate in window space
     * @param wheelRotation integer wheel rotation reported by AWT
     * @param preciseWheelRotation precise wheel rotation reported by AWT
     * @param mainEvent original AWT event
     */
    public JFWheelEvent(
            JFComponent source,
            int mouseX,
            int mouseY,
            int wheelRotation,
            double preciseWheelRotation,
            MouseWheelEvent mainEvent
    ) {
        this.source = source;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.wheelRotation = wheelRotation;
        this.preciseWheelRotation = preciseWheelRotation;
        this.mainEvent = mainEvent;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Returns the component that received the wheel event.
     *
     * @return event source component
     */
    public JFComponent getSource() {
        return source;
    }

    /**
     * Returns the pointer x-coordinate in window space.
     *
     * @return window-space x-coordinate
     */
    public int getMouseX() {
        return mouseX;
    }

    /**
     * Returns the pointer y-coordinate in window space.
     *
     * @return window-space y-coordinate
     */
    public int getMouseY() {
        return mouseY;
    }

    /**
     * Returns the integral wheel rotation reported by AWT.
     *
     * @return integer wheel delta
     */
    public int getWheelRotation() {
        return wheelRotation;
    }

    /**
     * Returns the high-precision wheel rotation reported by AWT.
     *
     * @return precise wheel delta
     */
    public double getPreciseWheelRotation() {
        return preciseWheelRotation;
    }

    /**
     * Returns the original AWT mouse-wheel event.
     *
     * @return wrapped raw event
     */
    public MouseWheelEvent getMainEvent() {
        return mainEvent;
    }

    /**
     * Returns the event creation timestamp in milliseconds.
     *
     * @return UNIX timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }
}
