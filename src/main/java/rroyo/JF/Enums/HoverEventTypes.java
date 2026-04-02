package rroyo.JF.Enums;

/**
 * Enumerates the lifecycle stages of pointer hover interactions.
 * <p>
 * A component can react differently when the pointer first enters its area, while it moves
 * inside that area, or when it leaves it. This enum gives the event system a compact and
 * readable way to describe those transitions.
 *
 * @author rroyo
 */
public enum HoverEventTypes {
    ENTER,
    MOVE,
    EXIT
}
