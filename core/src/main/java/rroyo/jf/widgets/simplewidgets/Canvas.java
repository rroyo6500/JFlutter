package rroyo.jf.widgets.simplewidgets;

import rroyo.jf.interfaces.Painter;
import rroyo.jf.widgets.basewidgets.Widget;

import java.awt.*;
import java.util.Objects;

public class Canvas extends Widget {

    private final Painter painter;

    public Canvas(Painter painter) {
        this.painter = painter;
    }

    @Override
    protected void layoutRecalculate() {
        if (father != null && Objects.equals(getSize(), new Dimension()))
            setSize(father.getSize());

    }

    @Override
    protected final void design(Graphics g) {
        if (painter != null) {
            painter.paint(g, getSize());
        }
    }
}
