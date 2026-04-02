package rroyo.JF.Enums;

/**
 * Enumerates the pointer-driven action states that can be emitted by interactive components.
 * <p>
 * The framework uses this enum to normalize raw AWT mouse activity into higher-level semantic
 * events that are easier to consume from custom components. A component listener does not need
 * to inspect the original Swing event to know whether the user clicked, pressed, or released
 * a button; that intent is already encoded in this enum.
 * <p>
 * Each enum constant can also temporarily store the mouse button that originated the event
 * through {@link #setButton(int)}. This design lets a
 * {@link rroyo.JF.JFEvents.JFActionEvent} expose both the action kind and the associated
 * button using a compact API.
 *
 * @author rroyo
 */
public enum ActionEventTypes {
    CLICK,
    DOWN,
    UP;

    /**
     * Mouse button associated with the current event occurrence.
     * <p>
     * Even though enum instances are shared singletons, the current implementation stores
     * the last button mapped for the dispatched event here so listeners can ask the action
     * itself which mouse button was involved.
     */
    private MouseButtons button;

    /**
     * Returns the logical mouse button currently associated with this action type.
     * <p>
     * This value is usually populated immediately before the action event is dispatched,
     * so listeners can inspect it without depending on the original Swing event object.
     *
     * @return bound mouse button, or {@code null} if no button has been assigned yet
     */
    public MouseButtons getButton() {
        return button;
    }

    /**
     * Converts an AWT mouse button code into the framework-specific {@link MouseButtons} enum.
     * <p>
     * This method is used while adapting Swing mouse events into framework action events.
     * It stores the converted button inside the enum instance and returns the same constant,
     * which makes it convenient to use inline when building a new action event object.
     *
     * @param button raw AWT mouse button code received from {@code MouseEvent#getButton()}
     * @return the same action type instance after binding the resolved button
     * @throws IllegalStateException when the supplied button code is not supported by the framework
     */
    public ActionEventTypes setButton(int button) {
        this.button = switch (button) {
            case 1 -> MouseButtons.LEFT;
            case 2 -> MouseButtons.MIDDLE;
            case 3 -> MouseButtons.RIGHT;
            default -> throw new IllegalStateException("Unexpected value: " + button);
        };
        return this;
    }
}
