package rroyo.jf.widgets.complexwidgets;

import rroyo.jf.enums.Alignment;
import rroyo.jf.widgets.basewidgets.ComplexWidget;
import rroyo.jf.widgets.basewidgets.Widget;
import static rroyo.jf.generated.Widgets.*;

import java.awt.*;

public class SizedStack extends ComplexWidget {

    private final Alignment alignment;
    private final Dimension dimension;
    private final Widget[] children;

    public SizedStack(Alignment alignment, Dimension dimension, Widget... children) {
        this.alignment = alignment;
        this.dimension = dimension;
        this.children = children;
    }

    @Override
    protected Widget build() {
        return SizedBox(dimension)
                .addChild(
                        Stack(alignment)
                                .addChilds(children)
                );
    }

}
