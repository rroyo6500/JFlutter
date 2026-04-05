package rroyo.JF.JFEvents;

import rroyo.JF.Enums.ActionEventTypes;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;

/**
 * Represents an action event that originates from a specific component.
 * This class encapsulates the source of the action, a description
 * or identifier of the action, and a timestamp indicating when the event occurred.
 *
 * @author rroyo
 */
public class JFActionEvent {

    /**
     * The component that originated this action event.
     * Represents the source of the event, typically a user interface component
     * that triggered the action.
     */
    private final JFComponent source;
    /**
     * Represents a description or identifier of an action associated with
     * a {@link JFActionEvent}. This variable typically indicates the type
     * or purpose of the action being performed, such as a command or
     * an operation triggered by a user interaction.
     */
    private final ActionEventTypes action;
    /**
     * Pointer x-coordinate in window space when the action happened.
     */
    private final int mouseX;
    /**
     * Pointer y-coordinate in window space when the action happened.
     */
    private final int mouseY;
    /**
     * Represents the timestamp indicating when the action event occurred.
     * This variable stores the time in milliseconds since the Unix epoch
     * (January 1, 1970, 00:00:00 GMT). It is initialized at the time of the
     * {@link JFActionEvent} object creation and serves as a chronological
     * reference for the event.
     */
    private final long timestamp;

    /**
     * Creates a new instance of {@code JFActionEvent}.
     * This constructor initializes the action event with the specified source component and action description.
     * The timestamp of the event is automatically set to the current system time in milliseconds.
     *
     * @param source The component that triggered this action event. Must not be null.
     * @param action A string representing the description or identifier of the action. Must not be null.
     */
    public JFActionEvent(JFComponent source, ActionEventTypes action) {
        this(source, action, -1, -1);
    }

    /**
     * Creates a new instance of {@code JFActionEvent} with pointer coordinates.
     *
     * @param source The component that triggered this action event. Must not be null.
     * @param action action type associated with the event. Must not be null.
     * @param mouseX pointer x-coordinate in window space
     * @param mouseY pointer y-coordinate in window space
     */
    public JFActionEvent(JFComponent source, ActionEventTypes action, int mouseX, int mouseY) {
        this.source = source;
        this.action = action;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Returns the component that emitted this event.
     *
     * @return event source component
     */
    public JFComponent getSource() {
        return source;
    }

    /**
     * Returns the action type associated with this event.
     *
     * @return action type
     */
    public ActionEventTypes getAction() {
        return action;
    }

    /**
     * Returns the pointer x-coordinate associated with this action, when available.
     *
     * @return window-space x-coordinate, or {@code -1} when not provided
     */
    public int getMouseX() {
        return mouseX;
    }

    /**
     * Returns the pointer y-coordinate associated with this action, when available.
     *
     * @return window-space y-coordinate, or {@code -1} when not provided
     */
    public int getMouseY() {
        return mouseY;
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
