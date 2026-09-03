package rroyo.jf.enums;

import java.awt.*;

public enum Alignment {
    CENTER,
    TOP,
    BOTTOM,
    LEFT,
    RIGHT,
    CUSTOM;

    public Point calculatePosition(Rectangle box, int childWidth, int childHeight) {
        return switch (this) {
            case CENTER -> new Point((box.width / 2) - (childWidth / 2), (box.height / 2) - (childHeight / 2));
            case TOP -> new Point((box.width / 2) - (childWidth / 2), 0);
            case BOTTOM -> new Point((box.width / 2) - (childWidth / 2), box.height - childHeight);
            case LEFT -> new Point(0, (box.height / 2) - (childHeight / 2));
            case RIGHT -> new Point(box.width - childWidth, (box.height / 2) - (childHeight / 2));
            case CUSTOM -> new Point(0 , 0);
        };
    }

}
