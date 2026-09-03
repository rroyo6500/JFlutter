package rroyo.jf.widgets.simplewidgets;

import rroyo.JUtils.Utils.Logging.LoggerAux;
import rroyo.jf.widgets.basewidgets.Widget;
import rroyo.jf.widgets.childcomponent.SingleChildComponent;

import java.awt.*;

public class SizedBox extends Widget implements SingleChildComponent<SizedBox> {

    public SizedBox(int width, int height) {
        if (width < 0 || height < 0)
            LoggerAux.warn("Dimensions cannot be negative");

        setSize(width, height);
    }

    public SizedBox(Dimension dimension) {
        this(dimension.width, dimension.height);
    }

    @Override
    protected void layoutRecalculate() {

    }

    @Override
    protected void design(Graphics g) {

    }
}
