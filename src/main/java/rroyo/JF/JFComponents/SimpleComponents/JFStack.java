package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.Enums.Alignment;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

/**
 * Layered container that stacks children using a shared bounding area.
 *
 * @author rroyo
 */
public class JFStack extends JFComponent {

    /**
     * Preferred alignment reference for stacked children.
     */
    private Alignment alignment;

    /**
     * Creates a stack component with the requested alignment strategy.
     *
     * @param alignment alignment mode used by the stack layout
     */
    public JFStack(Alignment alignment) {
        super(true);
        this.alignment = alignment;
    }

    public JFStack addChilds(@NotNull JFComponent... children) {
        for (JFComponent child : children) {
            super.addChild(child);
        }
        return this;
    }

    @Override
    protected void layoutRecalculate() {
        if (parent.getWidth() != 0 && parent.getHeight() != 0)
            setSize(parent.getWidth(), parent.getHeight());
        else {
            int maxWidth = 0;
            int maxHeight = 0;
            for (JFComponent child : childList) {
                maxWidth = Math.max(maxWidth, child.getWidth());
                maxHeight = Math.max(maxHeight, child.getHeight());
            }
            setSize(maxWidth, maxHeight);
        }

        if (alignment == Alignment.CUSTOM) return;
        for (JFComponent child : childList) {
            int[] position = alignment.calculatePosition(this.componentBox, child.getWidth(), child.getHeight());
            child.setPosition(position[0], position[1]);
        }
    }

    @Override
    protected void design(Graphics g) {

    }
}
