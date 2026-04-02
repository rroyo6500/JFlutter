package rroyo.JF.JFEvents;

import rroyo.JF.Enums.HoverEventTypes;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;

/**
 * Represents hover interactions for a component.
 *
 * @author rroyo
 */
public class JFHoverEvent {

    /**
     * Component currently associated with the hover event.
     */
    private final JFComponent source;
    /**
     * Pointer x-coordinate in component space.
     */
    private final int mouseX;
    /**
     * Pointer y-coordinate in component space.
     */
    private final int mouseY;
    /**
     * Hover event category (enter, move, or exit).
     */
    private final HoverEventTypes type;
    /**
     * Event creation timestamp in milliseconds.
     */
    private final long timestamp;

    /**
     * Creates a hover event for a component and pointer position.
     *
     * @param source component that receives the hover event
     * @param mouseX pointer x-coordinate
     * @param mouseY pointer y-coordinate
     * @param type hover event type
     */
    public JFHoverEvent(JFComponent source, int mouseX, int mouseY, HoverEventTypes type) {
        this.source = source;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Returns the component associated with this hover event.
     *
     * @return event source component
     */
    public JFComponent getSource() {
        return source;
    }

    /**
     * Returns the pointer x-coordinate.
     *
     * @return pointer x-coordinate
     */
    public int getMouseX() {
        return mouseX;
    }

    /**
     * Returns the pointer y-coordinate.
     *
     * @return pointer y-coordinate
     */
    public int getMouseY() {
        return mouseY;
    }

    /**
     * Returns the hover event type.
     *
     * @return hover event category
     */
    public HoverEventTypes getType() {
        return type;
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
