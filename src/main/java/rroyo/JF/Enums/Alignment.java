package rroyo.JF.Enums;

import java.awt.*;

/**
 * Generic alignment options for stack-based and anchored layouts.
 *
 * @author rroyo
 */
public enum Alignment {
    CENTER,
    TOP,
    BOTTOM,
    LEFT,
    RIGHT,
    CUSTOM;

    public int[] calculatePosition(Rectangle box, int childWidth, int childHeight) {
        return switch (this) {
            case CENTER -> new int[]{
                    (box.width / 2) - (childWidth / 2),
                    (box.height / 2) - (childHeight / 2)
                };
            case TOP -> new int[]{
                    (box.width / 2) - (childWidth / 2),
                    0
            };
            case BOTTOM -> new int[]{
                    (box.width / 2) - (childWidth / 2),
                    box.height - childHeight
            };
            case LEFT -> new int[]{
                    0,
                    (box.height / 2) - (childHeight / 2)
            };
            case RIGHT -> new int[]{
                    box.width - childWidth,
                    (box.height / 2) - (childHeight / 2)
            };
            case CUSTOM -> new int[]{0,0};
        };
    }

}
