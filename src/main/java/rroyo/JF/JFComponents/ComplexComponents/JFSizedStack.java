package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.Enums.Alignment;
import rroyo.JF.JFComponents.JFComplexComponent;
import rroyo.JF.JFComponents.JFComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFSizedBox;
import rroyo.JF.JFComponents.SimpleComponents.JFStack;

import java.util.function.Supplier;

public class JFSizedStack extends JFComplexComponent {

    public JFSizedStack(Alignment alignment, int width, int height, JFComponent... children) {
        super(() -> new JFSizedBox(width, height).addChild(
                new JFStack(alignment).addChilds(children)
        ));
    }

}
