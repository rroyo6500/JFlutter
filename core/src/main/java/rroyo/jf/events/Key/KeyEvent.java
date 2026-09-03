package rroyo.jf.events.Key;

import rroyo.jf.enums.KeyEventTypes;
import rroyo.jf.widgets.basewidgets.Widget;

public class KeyEvent {

    private final Widget source;

    private final int keyCode;

    private final char keyChar;

    private final KeyEventTypes type;

    private final java.awt.event.KeyEvent mainEvent;

    private final long timestamp;

    public KeyEvent(Widget source, KeyEventTypes type, int keyCode, char keyChar, java.awt.event.KeyEvent keyEvent) {
        this.source = source;
        this.type = type;
        this.keyCode = keyCode;
        this.keyChar = keyChar;
        this.mainEvent = keyEvent;
        this.timestamp = System.currentTimeMillis();
    }

    public Widget getSource() {
        return source;
    }

    public KeyEventTypes getType() {
        return type;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public char getKeyChar() {
        return keyChar;
    }

    public java.awt.event.KeyEvent getMainEvent() {
        return mainEvent;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
