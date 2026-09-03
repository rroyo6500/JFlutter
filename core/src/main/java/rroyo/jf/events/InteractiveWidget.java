package rroyo.jf.events;

import rroyo.jf.widgets.basewidgets.Widget;

public interface InteractiveWidget<T extends Widget> extends ActionWidget<T>, HoverWidget<T>, KeyWidget<T>, WheelWidget<T> {
}
