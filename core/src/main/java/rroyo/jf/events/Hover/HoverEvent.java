package rroyo.jf.events.Hover;

import rroyo.jf.enums.HoverEventTypes;
import rroyo.jf.widgets.basewidgets.Widget;

public class HoverEvent {

    private final Widget source;

    private final int mouseX;

    private final int mouseY;

    private final HoverEventTypes type;

    private final long timestamp;

    public HoverEvent(Widget source, int mouseX, int mouseY, HoverEventTypes type) {
        this.source = source;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    public Widget getSource() {
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
