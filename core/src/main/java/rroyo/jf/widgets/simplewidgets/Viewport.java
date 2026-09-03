package rroyo.jf.widgets.simplewidgets;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rroyo.jf.widgets.basewidgets.Widget;

import java.awt.*;

public class Viewport extends SizedBox {

    @Getter
    private int scrollX;
    @Getter
    private int scrollY;

    public Viewport(int width, int height) {
        super(width, height);
        setClipChildrenToBounds(true);
    }

    public Viewport(Dimension dimension) {
        super(dimension);
    }

    public Viewport setScrollX(int scrollX) {
        return setScroll(scrollX, scrollY);
    }

    public Viewport setScrollY(int scrollY) {
        return setScroll(scrollX, scrollY);
    }

    public Viewport setScroll(int scrollX, int scrollY) {
        this.scrollX = Math.max(0, scrollX);
        this.scrollY = Math.max(0, scrollY);
        positionChild();
        invalidateLayout();
        return this;
    }

    @Override
    public Widget setSize(int width, int height) {
        super.setSize(width, height);
        positionChild();
        return this;
    }

    private void positionChild() {
        Widget child = getChild();
        if (child == null) {
            return;
        }

        child.setPosition(-scrollX, -scrollY);
    }

    @Override
    protected void draw(Graphics g) {
        if (!canDraw()) return;

        Graphics2D g2d = (Graphics2D) g.create();
        try {
            g2d.clipRect(bounds.x, bounds.y, bounds.width, bounds.height);
            design(g2d);
            Widget child = getChild();
            if (child != null) {
                child.drawTree(g2d);
            }
        } finally {
            g2d.dispose();
        }
    }

    @Override
    protected void layoutRecalculate() {
        positionChild();
    }

    @Override
    public SizedBox addChild(@Nullable String id, @NotNull Widget child) {
        super.addChild(id, child);
        child.setOverflow(true);
        return this;
    }
}
