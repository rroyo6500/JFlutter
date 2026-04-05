package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.Enums.Alignment;
import rroyo.JF.JFComponents.BaseComponent.JFComplexComponent;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;

/**
 * Wrapper component that adds empty space around a single child.
 * <p>
 * {@code JFMargin} increases the outer size of the wrapped content by the requested
 * offsets and then repositions the child inside that new box. It behaves similarly to
 * a margin or padding helper in other UI toolkits, but is implemented as a reusable
 * complex component built from a fixed-size stack.
 *
 * @author rroyo
 */
public class JFMargin extends JFComplexComponent {

    /**
     * Creates a margin wrapper with the same value on all four sides.
     *
     * @param margin uniform margin applied left, right, top and bottom
     * @param content wrapped child component
     */
    public JFMargin(int margin, JFComponent content) {
        this(margin, margin, margin, margin, content);
    }

    /**
     * Creates a margin wrapper with explicit values for each side.
     *
     * @param marginLeft left-side margin in pixels
     * @param marginRight right-side margin in pixels
     * @param marginUp top margin in pixels
     * @param marginDown bottom margin in pixels
     * @param content wrapped child component
     */
    public JFMargin(int marginLeft, int marginRight, int marginUp, int marginDown, JFComponent content) {
        super(() -> {
            JFSizedStack marginBox = new JFSizedStack(Alignment.CUSTOM,
                    content.getWidth() + (marginLeft + marginRight),
                    content.getHeight() + (marginUp + marginDown)
            );
            marginBox.addChild(content);
            content.setPosition(marginLeft, marginUp);

            return marginBox;
        });
    }
}
