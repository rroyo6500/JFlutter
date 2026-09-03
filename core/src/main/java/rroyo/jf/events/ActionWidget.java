package rroyo.jf.events;

import rroyo.jf.events.Action.ActionEvent;
import rroyo.jf.events.Action.ActionListener;
import rroyo.jf.widgets.basewidgets.Widget;

import java.util.ArrayList;
import java.util.List;

public interface ActionWidget<T extends Widget> {

    EventDispatcher getEventDispatcher();

    @SuppressWarnings("unchecked")
    default T addActionListener(ActionListener listener) {
        if (listener == null) throw new IllegalArgumentException("Listener cannot be null");
        this.getEventDispatcher().addListener(ActionListener.class, listener);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T removeActionListener(ActionListener listener) {
        this.getEventDispatcher().removeListener(ActionListener.class, listener);
        return (T) this;
    }

    default void dispatchActionEvent(ActionEvent event) {
        List<ActionListener> listeners = this.getEventDispatcher().getListeners(ActionListener.class);
        for (ActionListener listener : listeners) {
            listener.actionPerformed(event);
        }
    }
}
