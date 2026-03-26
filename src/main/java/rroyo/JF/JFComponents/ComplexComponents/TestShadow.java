package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.Decorations.Border;
import rroyo.JF.Decorations.BoxShadow;
import rroyo.JF.Decorations.Decoration;
import rroyo.JF.JFComponents.JFComplexComponent;
import rroyo.JF.JFComponents.JFComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFContainer;

import java.awt.*;

public class TestShadow extends JFComplexComponent {

    public TestShadow(Color color) {
        super(new JFContainer(100, 100,
                new Decoration(color)
                        .setBorder(new Border(Color.black, 2))
                        .setShadow(new BoxShadow(Color.gray, 10, 10))
        ));
    }

}
