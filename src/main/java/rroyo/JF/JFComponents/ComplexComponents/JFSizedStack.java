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

    /**
     * Creates a fixed-size stack with the requested alignment and children.
     *
     * @param alignment alignment used by the internal stack for non-custom child positioning
     * @param width fixed width of the resulting component
     * @param height fixed height of the resulting component
     */
    public JFSizedStack(Alignment alignment, int width, int height) {
        super(() -> new JFSizedBox(width, height).addChild(
                new JFStack(alignment)
        ));
    }

    public JFSizedStack addChilds(JFComponent... children) {
        ((JFStack) getChild().getChild()).addChilds(children);
        return this;
    }

}
