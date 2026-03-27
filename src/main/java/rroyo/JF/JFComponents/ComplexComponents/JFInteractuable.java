package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.JFComponents.JFComplexComponent;
import rroyo.JF.JFComponents.JFComponent;
import rroyo.JF.JFEvents.JFInteractiveComponent;

public class JFInteractuable extends JFComplexComponent implements JFInteractiveComponent {

    public JFInteractuable(JFComponent content) {
        super(() -> content);
    }

}
