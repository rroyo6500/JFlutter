package rroyo.JF.JFEvents;

/**
 * Functional listener interface for framework keyboard events.
 * <p>
 * Components that can gain focus register listeners of this type so they can react to
 * typed characters, key presses and key releases routed by the active window.
 *
 * @author rroyo
 */
public interface JFKeyListener {

    /**
     * Handles a keyboard event dispatched to the focused component.
     *
     * @param e key event wrapper containing the focused source, logical type and raw AWT event
     */
    void actionPerformed(JFKeyEvent e);

}
