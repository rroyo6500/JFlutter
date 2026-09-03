package rroyo.jf.widgets.simplewidgets;

import rroyo.jf.enums.Alignment;
import rroyo.jf.widgets.basewidgets.Widget;
import rroyo.jf.widgets.childcomponent.MultiChildComponent;

public class Stack extends AlignWidget implements MultiChildComponent<Stack> {

    public Stack(Alignment alignment) {
        super(alignment);
    }

}
