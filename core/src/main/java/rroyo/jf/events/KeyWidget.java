package rroyo.jf.events;

import rroyo.jf.events.Action.ActionListener;
import rroyo.jf.events.Key.KeyEvent;
import rroyo.jf.events.Key.KeyListener;
import rroyo.jf.widgets.basewidgets.Widget;

import java.util.ArrayList;
import java.util.List;

public interface KeyWidget<T extends Widget> {

    EventDispatcher getEventDispatcher();

    @SuppressWarnings("unchecked")
    default T addKeyListener(KeyListener listener) {
        if (listener == null) throw new IllegalArgumentException("Listener cannot be null");
        this.getEventDispatcher().addListener(KeyListener.class, listener);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T removeKeyListener(KeyListener listener) {
        this.getEventDispatcher().removeListener(KeyListener.class, listener);
        return (T) this;
    }

    default void dispatchKeyEvent(KeyEvent event) {
        List<KeyListener> listeners = this.getEventDispatcher().getListeners(KeyListener.class);
        for (KeyListener listener : listeners) {
            listener.actionPerformed(event);
        }
    }

}
