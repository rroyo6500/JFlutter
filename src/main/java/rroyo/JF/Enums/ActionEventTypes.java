package rroyo.JF.Enums;

/**
 * Supported action event categories emitted by interactive components.
 *
 * @author rroyo
 */
public enum ActionEventTypes {
    CLICK,
    DOWN,
    UP;

    /**
     * Mouse button associated with the current action type instance.
     */
    private MouseButtons button;

    /**
     * Returns the mouse button bound to this action instance.
     *
     * @return bound mouse button
     */
    public MouseButtons getButton() {
        return button;
    }

    /**
     * Maps an AWT mouse button code to the internal button enum.
     *
     * @param button AWT mouse button code
     * @return current action type instance
     * @throws IllegalStateException when the button code is not supported
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
