package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.JFComponents.BaseComponent.JFComplexComponent;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;
import rroyo.JF.JFEvents.JFInteractiveComponent;

/**
 * Generic wrapper that turns any single child component into an interactive component.
 * <p>
 * This class is useful when the visual structure of a component already exists, but the caller
 * wants to attach action, hover or keyboard listeners without creating a dedicated custom class.
 * It contributes no custom drawing of its own and simply delegates its size to the wrapped content.
 *
 * @author rroyo
 */
public class JFInteractuable extends JFComplexComponent implements JFInteractiveComponent {

    /**
     * Wraps the supplied content so it can participate in the framework interaction system.
     *
     * @param content component to expose as interactive content
     */
    public JFInteractuable(JFComponent content) {
        super(() -> content);
    }

}
