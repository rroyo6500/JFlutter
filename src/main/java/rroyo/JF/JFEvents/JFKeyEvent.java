package rroyo.JF.JFEvents;

import rroyo.JF.Enums.KeyEventTypes;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;

import java.awt.event.KeyEvent;

/**
 * Wraps a raw AWT keyboard event together with framework-specific metadata.
 * <p>
 * The window creates instances of this class when a key event is received and forwards them
 * to the component that currently owns focus. The wrapper exposes the most commonly consumed
 * values directly, while still preserving access to the original AWT event for advanced cases.
 *
 * @author rroyo
 */
public class JFKeyEvent {

    /**
     * Component that owned focus when the key event was dispatched.
     */
    private final JFComponent source;

    /**
     * Numeric virtual key code reported by AWT for the event.
     */
    private final int keyCode;
    /**
     * Character representation reported by AWT for the event.
     */
    private final char keyChar;

    /**
     * Framework-level keyboard event category.
     */
    private final KeyEventTypes type;

    /**
     * Original AWT keyboard event kept for low-level access when needed.
     */
    private final KeyEvent mainEvent;

    /**
     * Creation time of the wrapper in milliseconds since the Unix epoch.
     */
    private final long timestamp;

    /**
     * Creates a new keyboard event wrapper from the currently focused component and a raw AWT event.
     *
     * @param source focused component that should receive the event
     * @param type framework event category associated with the dispatch
     * @param keyEvent original AWT keyboard event
     */
    public JFKeyEvent(JFComponent source, KeyEventTypes type, int keyCode, char keyChar, KeyEvent keyEvent) {
        this.source = source;
        this.type = type;
        this.keyCode = keyCode;
        this.keyChar = keyChar;
        this.mainEvent = keyEvent;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Returns the component that received the keyboard event.
     *
     * @return focused source component
     */
    public JFComponent getSource() {
        return source;
    }

    /**
     * Returns the logical keyboard event category.
     *
     * @return framework key event type
     */
    public KeyEventTypes getType() {
        return type;
    }

    /**
     * Returns the virtual key code extracted from the raw AWT event.
     *
     * @return AWT key code
     */
    public int getKeyCode() {
        return keyCode;
    }

    /**
     * Returns the character extracted from the raw AWT event.
     *
     * @return typed or associated character
     */
    public char getKeyChar() {
        return keyChar;
    }

    /**
     * Returns the original AWT event wrapped by this framework event.
     *
     * @return raw keyboard event
     */
    public KeyEvent getMainEvent() {
        return mainEvent;
    }

    /**
     * Returns the wrapper creation timestamp.
     *
     * @return event timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }

}
