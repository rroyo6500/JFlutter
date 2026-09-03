package rroyo.jf.events;

import rroyo.jf.events.Wheel.WheelEvent;
import rroyo.jf.events.Wheel.WheelListener;
import rroyo.jf.widgets.basewidgets.Widget;

import java.util.List;

public interface WheelWidget<T extends Widget> {

    EventDispatcher getEventDispatcher();

    @SuppressWarnings("unchecked")
    default T addWheelListener(WheelListener listener) {
        if (listener == null) throw new IllegalArgumentException("Listener cannot be null");
        this.getEventDispatcher().addListener(WheelListener.class, listener);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T removeWheelListener(WheelListener listener) {
        this.getEventDispatcher().removeListener(WheelListener.class, listener);
        return (T) this;
    }

    default void dispatchWheelEvent(WheelEvent event) {
        List<WheelListener> listeners = this.getEventDispatcher().getListeners(WheelListener.class);
        for (WheelListener listener : listeners) {
            listener.wheelMoved(event);
        }
    }
}
