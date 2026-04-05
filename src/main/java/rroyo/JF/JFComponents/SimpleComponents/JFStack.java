package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.Enums.Alignment;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;
import rroyo.JF.JFComponents.BaseComponent.JFMultiChildComponent;

import java.awt.*;

/**
 * Layered layout container that places all children within the same shared bounds.
 * <p>
 * By default, children are positioned according to a single alignment strategy so they stack
 * on top of each other. When the alignment is {@link Alignment#CUSTOM}, the stack preserves
 * the positions assigned directly to each child.
 *
 * @author rroyo
 */
public class JFStack extends JFComponent implements JFMultiChildComponent<JFStack> {

    /**
     * Alignment rule used to position children automatically inside the stack.
     */
    private Alignment alignment;

    /**
     * Creates a stack using the supplied alignment mode.
     *
     * @param alignment alignment mode applied to children when layout runs
     */
    public JFStack(Alignment alignment) {
        super(true);
        this.alignment = alignment;
    }

    /**
     * Adds a single child to the stack.
     *
     * @param child child to layer inside the stack
     * @return current stack
     */
    @Override
    public JFStack addChild(@NotNull JFComponent child) {
        attachChild(child);
        return this;
    }

    /**
     * Adds multiple children to the stack in the given order.
     *
     * @param children children to layer inside the stack
     * @return current stack
     */
    @Override
    public JFStack addChilds(@NotNull JFComponent... children) {
        for (JFComponent child : children) {
            addChild(child);
        }
        return this;
    }

    /**
     * Computes the stack size and positions children according to the configured alignment.
     * <p>
     * When the parent has an explicit size, the stack stretches to fill it. Otherwise it wraps
     * the maximum width and height of its children. Automatic child positioning is skipped in
     * custom mode so callers can manage layer positions manually.
     */
    @Override
    protected void layoutRecalculate() {
        if (parent.getWidth() != 0 && parent.getHeight() != 0)
            setSize(parent.getWidth(), parent.getHeight());
        else {
            int maxWidth = 0;
            int maxHeight = 0;
            for (JFComponent child : childList) {
                if (!child.isActive()) continue;

                maxWidth = Math.max(maxWidth, child.getWidth());
                maxHeight = Math.max(maxHeight, child.getHeight());
            }
            setSize(maxWidth, maxHeight);
        }

        if (alignment == Alignment.CUSTOM) return;
        for (JFComponent child : childList) {
            if (!child.isActive()) continue;

            Point position = alignment.calculatePosition(this.componentBox, child.getWidth(), child.getHeight());
            child.setPosition(position.x, position.y);
        }

    }

    /**
     * Stacks do not paint their own surface; only the layered children are rendered.
     *
     * @param g graphics context supplied during the paint pass
     */
    @Override
    protected void design(Graphics g) {

    }
}
