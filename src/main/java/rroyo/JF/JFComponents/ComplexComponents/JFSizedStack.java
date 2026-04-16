package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.Enums.Alignment;
import rroyo.JF.JFComponents.BaseComponent.JFComplexComponent;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;
import rroyo.JF.JFComponents.BaseComponent.JFMultiChildComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFSizedBox;
import rroyo.JF.JFComponents.SimpleComponents.JFStack;

/**
 * Convenience component that combines a fixed-size box with a stack layout.
 * <p>
 * The class is handy when several layered children must share an explicit width and height,
 * regardless of their own preferred sizes. Internally it creates a {@link JFSizedBox} and
 * places a {@link JFStack} inside it, exposing the composition as a single reusable component.
 *
 * @author rroyo
 */
public class JFSizedStack extends JFComplexComponent implements JFMultiChildComponent<JFSizedStack> {

    private final JFStack stack;

    public JFSizedStack(Alignment alignment, int width, int height) {
        this(
                new JFSizedBox(width, height),
                new JFStack(alignment)
        );
    }

    private JFSizedStack(JFSizedBox sizedBox, JFStack stack) {
        super(() -> sizedBox.addChild(stack));
        this.stack = stack;
    }

    /**
     * Forwards a child to the internal stack hosted inside the sized box.
     *
     * @param child child to layer inside the fixed-size stack
     * @return current sized stack
     */
    @Override
    public JFSizedStack addChild(JFComponent child) {
        stack.addChild(child);
        return this;
    }

    /**
     * Forwards multiple children to the internal stack in insertion order.
     *
     * @param children children to layer inside the fixed-size stack
     * @return current sized stack
     */
    @Override
    public JFSizedStack addChilds(JFComponent... children) {
        stack.addChilds(children);
        return this;
    }

}
