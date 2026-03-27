package rroyo.CustomComponents;

import rroyo.JF.Enums.ActionEventTypes;
import rroyo.JF.JFComponents.ComplexComponents.JFButton;
import rroyo.JF.JFComponents.JFComplexComponent;
import rroyo.JF.JFComponents.JFComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFContainer;
import rroyo.JF.JFComponents.SimpleComponents.JFText;

import java.awt.*;
import java.util.function.Supplier;

public class ColorButton extends JFComplexComponent {

    public ColorButton(DrawingBoard drawingBoard, Color color) {
        super(() ->
                new JFButton(0, 0, null, null)
                        .addActionListener(e -> {
                            if (e.getAction() == ActionEventTypes.CLICK)
                                drawingBoard.setCurrentColor(color);
                        })
                        .redesignContent(
                                new JFContainer(50, 50, color)
                        )
        );
    }
}
