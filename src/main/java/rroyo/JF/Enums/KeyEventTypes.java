package rroyo.JF.Enums;

/**
 * Enumerates the keyboard event categories exposed by the framework.
 * <p>
 * These values mirror the three standard AWT keyboard callbacks and are used when the
 * window translates raw Swing keyboard input into framework-level events targeted at
 * the currently focused component.
 *
 * @author rroyo
 */
public enum KeyEventTypes {
    KEY_PRESSED,
    KEY_RELEASED,
    KEY_TYPED
}
