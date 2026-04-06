package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.Enums.SelectionType;

/**
 * Backwards-compatible checkbox wrapper built on top of {@link JFSelectionControl}.
 *
 * @author rroyo
 */
public class JFCheckBox extends JFSelectionControl {

    /**
     * Creates a checkbox with the provided label text.
     *
     * @param text label shown next to the box
     */
    public JFCheckBox(String text) {
        super(SelectionType.CHECKBOX, text);
    }

    @Override
    public JFCheckBox setSelected(boolean selected) {
        super.setSelected(selected);
        return this;
    }

    @Override
    public JFCheckBox setText(String text) {
        super.setText(text);
        return this;
    }
}
