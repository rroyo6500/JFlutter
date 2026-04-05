package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;
import rroyo.JF.JFComponents.BaseComponent.JFMultiChildComponent;

import java.awt.*;

/**
 * Vertical layout container that always wraps its full content height.
 * <p>
 * Unlike {@link JFColumn}, this component does not stretch to the parent's height.
 * That makes it suitable as the content node inside a clipped viewport.
 *
 * @author rroyo
 */
public class JFScrollColumn extends JFComponent implements JFMultiChildComponent<JFScrollColumn> {

    /**
     * Creates an empty scroll column.
     */
    public JFScrollColumn() {
        super(true);
    }

    /**
     * Adds a child at the end of the scroll column.
     *
     * @param child child to append
     * @return current scroll column
     */
    @Override
    public JFScrollColumn addChild(@NotNull JFComponent child) {
        attachChild(child);
        return this;
    }

    /**
     * Adds multiple children in order.
     *
     * @param children children to append
     * @return current scroll column
     */
    @Override
    public JFScrollColumn addChilds(@NotNull JFComponent... children) {
        for (JFComponent child : children) {
            addChild(child);
        }
        return this;
    }

    /**
     * Stacks active children vertically and wraps the final content size.
     */
    @Override
    protected void layoutRecalculate() {
        int totalHeight = 0;
        int maxWidth = 0;

        for (JFComponent child : childList) {
            if (!child.isActive()) continue;

            child.setPosition(0, totalHeight);
            totalHeight += child.getHeight();
            maxWidth = Math.max(maxWidth, child.getWidth());
        }

        setSize(maxWidth, totalHeight);
    }

    /**
     * Scroll columns do not paint anything directly; only their children are rendered.
     *
     * @param g graphics context supplied during painting
     */
    @Override
    protected void design(Graphics g) {
    }
}
