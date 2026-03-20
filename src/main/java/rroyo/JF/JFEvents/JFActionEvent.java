package rroyo.JF.JFEvents;

import rroyo.JF.JFComponents.JFComponent;

/**
 * Represents an action event that originates from a specific component.
 * This class encapsulates the source of the action, a description
 * or identifier of the action, and a timestamp indicating when the event occurred.
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
    private final String action;
    /**
     * Represents the timestamp indicating when the action event occurred.
     * This variable stores the time in milliseconds since the Unix epoch
     * (January 1, 1970, 00:00:00 GMT). It is initialized at the time of the
     * {@link JFActionEvent} object creation and serves as a chronological
     * reference for the event.
     */
    private final long timestmap;

    /**
     * Creates a new instance of {@code JFActionEvent}.
     * This constructor initializes the action event with the specified source component and action description.
     * The timestamp of the event is automatically set to the current system time in milliseconds.
     *
     * @param source The component that triggered this action event. Must not be null.
     * @param action A string representing the description or identifier of the action. Must not be null.
     */
    public JFActionEvent(JFComponent source, String action) {
        this.source = source;
        this.action = action;
        this.timestmap = System.currentTimeMillis();
    }

    public JFComponent getSource() {
        return source;
    }

    public String getAction() {
        return action;
    }
}
