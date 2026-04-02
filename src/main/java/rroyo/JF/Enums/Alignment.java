package rroyo.JF.Enums;

import java.awt.*;

/**
 * Describes the alignment strategies available for components that position children relative
 * to a containing rectangle.
 * <p>
 * This enum is mainly used by stack-like layouts and helper containers that need a simple
 * way to translate an alignment choice into a pair of coordinates. Instead of duplicating
 * the same centering and edge-positioning arithmetic across the codebase, the framework
 * centralizes that logic here.
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

    /**
     * Calculates the local position a child component should take inside a reference rectangle.
     * <p>
     * The returned coordinates are relative to the top-left corner of {@code box}. They can
     * be fed directly into {@code setPosition(...)} on a child whose parent uses the supplied
     * rectangle as its own content area.
     *
     * @param box rectangle representing the available parent area
     * @param childWidth width of the child to place
     * @param childHeight height of the child to place
     * @return a two-element array containing {@code x} at index 0 and {@code y} at index 1
     */
    public Point calculatePosition(Rectangle box, int childWidth, int childHeight) {
        return switch (this) {
            case CENTER -> new Point((box.width / 2) - (childWidth / 2), (box.height / 2) - (childHeight / 2));
            case TOP -> new Point((box.width / 2) - (childWidth / 2), 0);
            case BOTTOM -> new Point((box.width / 2) - (childWidth / 2), box.height - childHeight);
            case LEFT -> new Point(0, (box.height / 2) - (childHeight / 2));
            case RIGHT -> new Point(box.width - childWidth, (box.height / 2) - (childHeight / 2));
            case CUSTOM -> new Point(0, 0);
        };
    }

}
