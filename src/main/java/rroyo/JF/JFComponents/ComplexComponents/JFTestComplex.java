package rroyo.JF.JFComponents.ComplexComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComplexComponent;
import rroyo.JF.JFComponents.JFComponent;
import rroyo.JF.JFComponents.SimpleComponents.*;

import java.awt.*;

public class JFTestComplex extends JFComplexComponent {

    public JFTestComplex() {
        super(
                new JFSizeBox().setSize(100, 100).addChild(
                        new JFCenter(
                                new JFColumn().addChilds(
                                        new JFContainer().setColor(Color.red).setSize(25, 25),
                                        new JFRow().addChilds(
                                                new JFContainer().setColor(Color.blue).setSize(25, 25),
                                                new JFColumn().addChilds(
                                                        new JFContainer().setColor(Color.green).setSize(25, 25),
                                                        new JFContainer().setColor(Color.yellow).setSize(25, 25)
                                                )
                                        )
                                )
                        )
                )
        );
    }

}
