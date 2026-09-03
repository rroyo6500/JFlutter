package rroyo.jf.widgets.simplewidgets;

import rroyo.jf.enums.FlexDirection;
import rroyo.jf.widgets.basewidgets.Widget;

public class Column extends Flex{

    public Column() {
        super(FlexDirection.COLUMN);
    }

    public Column(Widget... children) {
        super(FlexDirection.COLUMN);
        addChilds(children);
    }

}
