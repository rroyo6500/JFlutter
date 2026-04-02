package rroyo.JF.JFEvents;

/**
 * Functional listener interface used to react to framework action events.
 * <p>
 * Components implementing {@link JFActionComponent} can register any number of listeners of
 * this type. Each listener is invoked whenever the component dispatches a click, press, or
 * release event represented by {@link JFActionEvent}.
 *
 * @author rroyo
 */
public interface JFActionListener {
    /**
     * Handles a new action event emitted by a component.
     *
     * @param e event object describing the source component, logical action and dispatch timestamp
     */
    void actionPerformed(JFActionEvent e);
}
