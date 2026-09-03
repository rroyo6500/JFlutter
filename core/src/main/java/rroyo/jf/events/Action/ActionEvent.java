package rroyo.jf.events.Action;

import lombok.Getter;
import rroyo.jf.enums.ActionEventTypes;
import rroyo.jf.enums.MouseButtons;
import rroyo.jf.widgets.basewidgets.Widget;
import rroyo.jf.widgets.simplewidgets.Window;

import java.awt.*;

public class ActionEvent {

    @Getter
    private final Widget source;

    @Getter
    private final Window window;

    @Getter
    private final ActionEventTypes action;

    @Getter
    private final MouseButtons mouseButton;

    @Getter
    private final Point mouseLocation;

    @Getter
    private final long timestamp;

    public ActionEvent(Widget source, ActionEventTypes action, int mouseButton, int mouseX, int mouseY) {
        this.source = source;
        this.window = source.getWindow();
        this.action = action;
        this.mouseButton = switch (mouseButton) {
            case 1 -> MouseButtons.LEFT;
            case 2 -> MouseButtons.MIDDLE;
            case 3 -> MouseButtons.RIGHT;
            default -> throw new IllegalStateException("Unexpected value: " + mouseButton);
        };
        this.mouseLocation = new Point(mouseX, mouseY);
        this.timestamp = System.currentTimeMillis();
    }

}
