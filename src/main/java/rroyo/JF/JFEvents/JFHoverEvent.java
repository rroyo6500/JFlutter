package rroyo.JF.JFEvents;

import rroyo.JF.JFComponents.JFComponent;

/**
 * Represents hover interactions for a component.
 */
public class JFHoverEvent {

    public enum Type {
        ENTER,
        MOVE,
        EXIT
    }

    private final JFComponent source;
    private final int mouseX;
    private final int mouseY;
    private final Type type;
    private final long timestamp;

    public JFHoverEvent(JFComponent source, int mouseX, int mouseY, Type type) {
        this.source = source;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    public JFComponent getSource() {
        return source;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public Type getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
