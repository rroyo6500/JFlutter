package rroyo.JF;

import rroyo.JF.Decorations.Border;
import rroyo.JF.Decorations.Decoration;
import rroyo.JF.Enums.HoverEventTypes;
import rroyo.JF.JFComponents.ComplexComponents.JFButton;
import rroyo.JF.JFComponents.JFComplexComponent;
import rroyo.JF.JFComponents.JFComponent;
import rroyo.JF.JFComponents.SimpleComponents.*;
import rroyo.JF.JFEvents.JFHoverEvent;

import java.awt.*;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        JFWindow window = new JFWindow(800, 800);

        window.addChild(
            new JFButton(100, 50, "Boton", Color.RED)
        );

    }

}
