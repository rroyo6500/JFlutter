package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;
import rroyo.JF.JFComponents.ChildComponents.JFMultiChildComponent;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Vertical layout container that always wraps its full content height.
 * <p>
 * Unlike {@link JFColumn}, this component does not stretch to the parent's height.
 * That makes it suitable as the content node inside a clipped viewport.
 *
 * @author rroyo
 */
public class JFScrollColumn extends JFComponent implements JFMultiChildComponent<JFScrollColumn> {

    private final Map<String, JFComponent> childMap = new LinkedHashMap<>();

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
    public Map<String, JFComponent> getChildMap() {
        return childMap;
    }

    /**
     * Stacks active children vertically and wraps the final content size.
     */
    @Override
    protected void layoutRecalculate() {
        int totalHeight = 0;
        int maxWidth = 0;

        for (JFComponent child : getChildList()) {
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
