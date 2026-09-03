package widgets;

import rroyo.jf.widgets.basewidgets.ComplexWidget;
import rroyo.jf.widgets.basewidgets.Widget;
import rroyo.jf.widgets.simplewidgets.Container;

import java.awt.*;

public class TestWidget extends ComplexWidget {
    @Override
    protected Widget build() {
        return new Container(100, 100, Color.red);
    }
}
