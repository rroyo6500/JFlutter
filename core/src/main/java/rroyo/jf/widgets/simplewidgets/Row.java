package rroyo.jf.widgets.simplewidgets;

import rroyo.jf.enums.FlexDirection;
import rroyo.jf.widgets.basewidgets.Widget;

public class Row extends Flex{

    public Row() {
        super(FlexDirection.ROW);
    }

    public Row(Widget... children) {
        super(FlexDirection.ROW);
        addChilds(children);
    }

}
