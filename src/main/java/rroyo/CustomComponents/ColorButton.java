package rroyo.CustomComponents;

import rroyo.JF.Enums.ActionEventTypes;
import rroyo.JF.JFComponents.ComplexComponents.JFInteractuable;
import rroyo.JF.JFComponents.JFComplexComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFContainer;

import java.awt.*;

public class ColorButton extends JFComplexComponent {

    public ColorButton(DrawingBoard drawingBoard, Color color) {
        super(() ->
                new JFInteractuable(
                    new JFContainer(50, 50, color)
                ).addActionListener(e -> {
                    if (e.getAction() == ActionEventTypes.CLICK)
                        drawingBoard.setCurrentColor(color);
                })
        );
    }
}
