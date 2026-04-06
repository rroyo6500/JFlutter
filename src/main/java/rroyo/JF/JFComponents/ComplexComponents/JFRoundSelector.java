package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.Enums.SelectionType;

/**
 * Backwards-compatible round selector wrapper built on top of {@link JFSelectionControl}.
 *
 * @author rroyo
 */
public class JFRoundSelector extends JFSelectionControl {

    /**
     * Creates a round selector with the provided label text.
     *
     * @param text label shown next to the round indicator
     */
    public JFRoundSelector(String text) {
        super(SelectionType.ROUND, text);
    }

    /**
     * Attaches this selector to a {@link JFGroup}.
     *
     * @param group target group, or {@code null} to detach
     * @return current selector
     */
    public JFRoundSelector setGroup(JFGroup group) {
        super.setGroup(group);
        return this;
    }

    @Override
    public JFRoundSelector setSelected(boolean selected) {
        super.setSelected(selected);
        return this;
    }

    @Override
    public JFRoundSelector setText(String text) {
        super.setText(text);
        return this;
    }
}
