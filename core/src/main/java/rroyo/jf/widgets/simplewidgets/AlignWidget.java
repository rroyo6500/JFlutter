package rroyo.jf.widgets.simplewidgets;

import rroyo.jf.enums.Alignment;
import rroyo.jf.widgets.basewidgets.Widget;
import rroyo.jf.widgets.childcomponent.MultiChildComponent;

import java.awt.*;

public abstract class AlignWidget extends Widget {

    protected final Alignment alignment;

    public AlignWidget(Alignment alignment) {
        super(true);
        this.alignment = alignment;
    }

    @Override
    protected void layoutRecalculate() {

        if (father.getSize().width != 0 && father.getSize().height != 0) {
            setSize(father.getSize());
        } else {
            int maxWidth = 0;
            int maxHeight = 0;
            for (Widget child : getChildrenMap().values()) {
                if (!child.isActive()) continue;

                maxWidth = Math.max(maxWidth, child.getSize().width);
                maxHeight = Math.max(maxHeight, child.getSize().height);
            }
            setSize(maxWidth, maxHeight);
        }

        for (Widget child : getChildrenMap().values()) {
            if (!child.isActive()) continue;

            if (alignment != Alignment.CUSTOM) {
                Point point = alignment.calculatePosition(this.bounds, child.getSize().width, child.getSize().height);
                child.setPosition(point);
            }
        }

    }

    @Override
    protected void design(Graphics g) {

    }
}
