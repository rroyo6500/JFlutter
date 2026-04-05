package rroyo.JF.JFEvents;

/**
 * Aggregates the three interactive capabilities offered by the framework.
 * <p>
 * A component implementing this interface can receive mouse action events, hover events,
 * and keyboard events through a single marker interface. This is useful for custom widgets
 * that should behave like fully interactive controls without repeating the three individual
 * event interfaces in their declaration.
 *
 * @author rroyo
 */
public interface JFInteractiveComponent extends JFActionComponent, JFHoverComponent, JFKeyComponent, JFWheelComponent {

}
