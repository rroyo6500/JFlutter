package rroyo.JF.Enums;

/**
 * Defines how children are distributed along the primary axis of a flex container.
 * <p>
 * In a row this axis is horizontal; in a column it is vertical. Besides classic start,
 * center and end positioning, the enum also provides spacing strategies that distribute
 * the remaining free space between or around children.
 *
 * @author rroyo
 */
public enum MainAxisAlignment {
    DEFAULT,
    START,
    CENTER,
    END,
    SPACE_BETWEEN,
    SPACE_AROUND,
    SPACE_EVENLY
}
