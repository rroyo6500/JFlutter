package rroyo.JF.JFEvents;

import rroyo.JF.Enums.HoverEventTypes;
import rroyo.JF.JFComponents.JFComponent;

/**
 * Represents hover interactions for a component.
 */
public class JFHoverEvent {

    private final JFComponent source;
    private final int mouseX;
    private final int mouseY;
    private final HoverEventTypes type;
    private final long timestamp;

    public JFHoverEvent(JFComponent source, int mouseX, int mouseY, HoverEventTypes type) {
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

    public HoverEventTypes getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
