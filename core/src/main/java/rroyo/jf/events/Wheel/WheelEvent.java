package rroyo.jf.events.Wheel;

import rroyo.jf.widgets.basewidgets.Widget;

import java.awt.event.MouseWheelEvent;

public class WheelEvent {

    private final Widget source;

    private final int mouseX;

    private final int mouseY;

    private final int wheelRotation;

    private final double preciseWheelRotation;

    private final MouseWheelEvent mainEvent;

    private final long timestamp;

    public WheelEvent(
            Widget source,
            int mouseX,
            int mouseY,
            int wheelRotation,
            double preciseWheelRotation,
            MouseWheelEvent mainEvent
    ) {
        this.source = source;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.wheelRotation = wheelRotation;
        this.preciseWheelRotation = preciseWheelRotation;
        this.mainEvent = mainEvent;
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

    public int getWheelRotation() {
        return wheelRotation;
    }

    public double getPreciseWheelRotation() {
        return preciseWheelRotation;
    }

    public MouseWheelEvent getMainEvent() {
        return mainEvent;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
