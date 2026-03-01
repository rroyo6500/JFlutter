package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.JFComponents.JFComplexComponent;
import rroyo.JF.JFComponents.JFComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFCenter;
import rroyo.JF.JFComponents.SimpleComponents.JFContainer;

import java.awt.*;

public class JFTestComplex extends JFComplexComponent {

    public JFTestComplex() {
        super(new JFContainer().setColor(Color.red).setSize(200, 200).addChild(
                new JFCenter(
                        new JFContainer().setColor(Color.blue).setSize(100, 100)
                )
        ));
    }

}
