package rroyo.JF.Enums;

/**
 * Defines how children are aligned on the secondary axis of a flex container.
 * <p>
 * In a row this axis is vertical; in a column it is horizontal. Keeping these values
 * in a dedicated enum makes the row and column implementations share the same alignment
 * vocabulary and layout calculations.
 *
 * @author rroyo
 */
public enum CrossAxisAlignment {
    DEFAULT,
    START,
    CENTER,
    END
}
