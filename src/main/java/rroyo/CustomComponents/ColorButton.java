package rroyo.CustomComponents;

import rroyo.JF.Enums.ActionEventTypes;
import rroyo.JF.JFComponents.ComplexComponents.JFInteractuable;
import rroyo.JF.JFComponents.BaseComponent.JFComplexComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFContainer;

import java.awt.*;

/**
 * Small color-palette button used by the demo drawing application.
 * <p>
 * Each instance renders a fixed-size colored square and, when clicked, updates the active
 * brush color of the associated {@link DrawingBoard}. It also updates an external preview
 * container so the selected color can be reflected elsewhere in the UI.
 *
 * @author rroyo
 */
public class ColorButton extends JFComplexComponent {

    /**
     * Color represented by this palette button.
     */
    public Color color;

    /**
     * Creates a palette button bound to a drawing board and an external color preview container.
     * <p>
     * The internal composition uses {@link JFInteractuable} so the colored square can react
     * to click events. When the square is clicked, the target drawing board changes its current
     * brush color and the provided container decoration is updated to the same color.
     *
     * @param container container whose decoration is used as an external selected-color preview
     * @param drawingBoard board whose active drawing color should be changed on click
     * @param color color represented by the button and applied when selected
     */
    public ColorButton(JFContainer container, DrawingBoard drawingBoard, Color color) {
        super(() ->
                new JFInteractuable(
                        new JFContainer(50, 50, color)
                ).addActionListener(e -> {
                    if (e.getAction() == ActionEventTypes.CLICK) {
                        drawingBoard.setCurrentColor(color);
                        container.getDecoration().setColor(color);
                    }
                })
        );
        this.color = color;
    }
}
