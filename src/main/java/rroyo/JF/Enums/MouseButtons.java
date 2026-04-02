package rroyo.JF.Enums;

/**
 * Identifies the logical mouse buttons recognized by the framework event system.
 * <p>
 * Raw AWT button codes are converted into these values before action events are delivered,
 * which keeps listener code independent from Swing-specific numeric constants.
 *
 * @author rroyo
 */
public enum MouseButtons {
    LEFT,
    RIGHT,
    MIDDLE
}
