package rroyo.jf.widgets.simplewidgets;

import rroyo.jf.enums.Alignment;
import rroyo.jf.widgets.childcomponent.SingleChildComponent;

public class Center extends AlignWidget implements SingleChildComponent<Center> {

    public Center() {
        super(Alignment.CENTER);
    }

}
