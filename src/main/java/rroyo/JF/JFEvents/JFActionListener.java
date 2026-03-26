package rroyo.JF.JFEvents;

/**
 * This interface defines a contract for handling action events.
 * Classes implementing this interface must provide an implementation
 * for reacting to {@link JFActionEvent} instances when triggered.
 * <br>
 * The primary purpose of implementing this interface is to process
 * user interactions or other events associated with components
 * that fire {@link JFActionEvent} objects.
 *
 * @author rroyo
 */
public interface JFActionListener {
    /**
     * Invoked when an action event occurs. Classes implementing this method
     * should define the behavior to handle the provided {@code JFActionEvent}.
     *
     * @param e the {@code JFActionEvent} containing information about the source,
     *          action description, and timestamp of the event
     */
    void actionPerformed(JFActionEvent e);
}
