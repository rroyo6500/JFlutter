package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;
import rroyo.JF.JFComponents.BaseComponent.JFSingleChildComponent;

import java.awt.*;

/**
 * Fixed-size clipping viewport that shows only a portion of a larger child.
 * <p>
 * The child can be larger than the viewport and is repositioned through the scroll
 * offsets. Drawing and hit testing are clipped to the viewport bounds, which makes
 * the component useful as the basis for scrollable widgets.
 *
 * @author rroyo
 */
public class JFViewport extends JFComponent implements JFSingleChildComponent<JFViewport> {

    private int scrollX;
    private int scrollY;

    /**
     * Creates a viewport with explicit size.
     *
     * @param width viewport width in pixels
     * @param height viewport height in pixels
     */
    public JFViewport(int width, int height) {
        setSize(width, height);
        setClipChildrenToBounds(true);
    }

    /**
     * Returns the current horizontal scroll offset.
     *
     * @return horizontal scroll offset in pixels
     */
    public int getScrollX() {
        return scrollX;
    }

    /**
     * Returns the current vertical scroll offset.
     *
     * @return vertical scroll offset in pixels
     */
    public int getScrollY() {
        return scrollY;
    }

    /**
     * Updates the viewport scroll offsets.
     *
     * @param scrollX new horizontal offset
     * @param scrollY new vertical offset
     * @return current viewport
     */
    public JFViewport setScroll(int scrollX, int scrollY) {
        this.scrollX = Math.max(0, scrollX);
        this.scrollY = Math.max(0, scrollY);
        positionChild();
        invalidateLayout();
        return this;
    }

    /**
     * Convenience helper to change only the vertical offset.
     *
     * @param scrollY new vertical offset
     * @return current viewport
     */
    public JFViewport setScrollY(int scrollY) {
        return setScroll(scrollX, scrollY);
    }

    @Override
    public JFViewport addChild(@NotNull JFComponent child) {
        clearChildren();
        child.setOverflowAllowed(true);
        attachChild(child);
        positionChild();
        return this;
    }

    @Override
    public JFViewport setSize(int width, int height) {
        super.setSize(width, height);
        positionChild();
        return this;
    }

    @Override
    protected void layoutRecalculate() {
        positionChild();
    }

    @Override
    protected void draw(Graphics g) {
        if (!canDraw()) return;

        Graphics2D g2d = (Graphics2D) g.create();
        try {
            g2d.clipRect(componentBox.x, componentBox.y, componentBox.width, componentBox.height);
            design(g2d);
            for (JFComponent child : childList) {
                child.drawTree(g2d);
            }
        } finally {
            g2d.dispose();
        }
    }

    @Override
    protected void design(Graphics g) {
    }

    private void positionChild() {
        if (childList.isEmpty()) {
            return;
        }

        childList.getFirst().setPosition(-scrollX, -scrollY);
    }
}
