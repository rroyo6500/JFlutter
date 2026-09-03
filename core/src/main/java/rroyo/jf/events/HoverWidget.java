package rroyo.jf.events;

import rroyo.jf.events.Hover.HoverEvent;
import rroyo.jf.events.Hover.HoverListener;
import rroyo.jf.events.Wheel.WheelListener;
import rroyo.jf.widgets.basewidgets.Widget;

import java.util.ArrayList;
import java.util.List;

public interface HoverWidget<T extends Widget> {

    EventDispatcher getEventDispatcher();

    @SuppressWarnings("unchecked")
    default T addHoverListener(HoverListener listener) {
        if (listener == null) throw new IllegalArgumentException("Listener cannot be null");
        this.getEventDispatcher().addListener(HoverListener.class, listener);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T removeHoverListener(HoverListener listener) {
        this.getEventDispatcher().removeListener(HoverListener.class, listener);
        return (T) this;
    }

    default void dispatchHoverEvent(HoverEvent event) {
        List<HoverListener> listeners = this.getEventDispatcher().getListeners(HoverListener.class);
        for (HoverListener listener : listeners) {
            listener.hoverEvent(event);
        }
    }
}
