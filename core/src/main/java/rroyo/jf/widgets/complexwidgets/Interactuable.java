package rroyo.jf.widgets.complexwidgets;

import rroyo.jf.events.ActionWidget;
import rroyo.jf.events.InteractiveWidget;
import rroyo.jf.widgets.basewidgets.ComplexWidget;
import rroyo.jf.widgets.basewidgets.Widget;

public class Interactuable extends ComplexWidget implements InteractiveWidget<Interactuable> {

    private final Widget child;

    public Interactuable(Widget child) {
        this.child = child;
    }

    @Override
    protected Widget build() {
        return child;
    }

}
