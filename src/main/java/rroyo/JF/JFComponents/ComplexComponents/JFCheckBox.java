package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.Decorations.Border;
import rroyo.JF.Decorations.BoxShadow;
import rroyo.JF.Enums.ActionEventTypes;
import rroyo.JF.Enums.HoverEventTypes;
import rroyo.JF.Enums.KeyEventTypes;
import rroyo.JF.JFComponents.BaseComponent.JFComplexComponent;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFContainer;
import rroyo.JF.JFComponents.SimpleComponents.JFRow;
import rroyo.JF.JFComponents.SimpleComponents.JFSizedBox;
import rroyo.JF.JFComponents.SimpleComponents.JFText;
import rroyo.JF.JFEvents.JFActionEvent;
import rroyo.JF.JFEvents.JFHoverListener;
import rroyo.JF.JFEvents.JFInteractiveComponent;
import rroyo.JF.JFEvents.JFKeyListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Checkbox-style selector with label, hover feedback and keyboard activation.
 * <p>
 * The component manages its own selected, hovered and pressed states, updates its visuals
 * accordingly and exposes change listeners for clients interested in value changes.
 *
 * @author rroyo
 */
public class JFCheckBox extends JFComplexComponent implements JFInteractiveComponent {

    private static final int BOX_SIZE = 20;
    private static final int LABEL_GAP = 10;
    private static final Font LABEL_FONT = new Font("Dialog", Font.PLAIN, 13);
    private static final Color SURFACE_COLOR = new Color(245, 247, 250);
    private static final Color SURFACE_HOVER_COLOR = new Color(236, 241, 247);
    private static final Color SURFACE_PRESSED_COLOR = new Color(222, 231, 241);
    private static final Color BORDER_COLOR = new Color(71, 85, 105);
    private static final Color ACTIVE_COLOR = new Color(37, 99, 235);
    private static final Color ACTIVE_PRESSED_COLOR = new Color(29, 78, 216);
    private static final Color LABEL_COLOR = new Color(30, 41, 59);

    private final JFContainer boxContainer;
    private final CheckMarkIcon markIcon;
    private final JFText label;
    private final List<Consumer<JFCheckBox>> changeListeners = new ArrayList<>();

    private boolean selected;
    private boolean hovered;
    private boolean pressed;

    /**
     * Creates a checkbox with the provided label text.
     *
     * @param text label shown next to the box
     */
    public JFCheckBox(String text) {
        this(createLabel(text), new JFContainer(BOX_SIZE, BOX_SIZE, SURFACE_COLOR), new CheckMarkIcon(BOX_SIZE));
    }

    /**
     * Builds the checkbox from preconfigured internal nodes.
     *
     * @param label text node used for the label
     * @param boxContainer clickable box surface
     * @param markIcon icon used for the selected check mark
     */
    private JFCheckBox(JFText label, JFContainer boxContainer, CheckMarkIcon markIcon) {
        super(() -> {
            boxContainer.getDecoration()
                    .setBorder(new Border(BORDER_COLOR, 1))
                    .setBorderRadius(6)
                    .setShadow(new BoxShadow(new Color(15, 23, 42, 35), 0, 2, 3));

            markIcon.setPosition(0, 0);
            boxContainer.addChild(markIcon);

            return new JFRow(
                    boxContainer,
                    new JFSizedBox(LABEL_GAP, 1),
                    label
            );
        });

        this.boxContainer = boxContainer;
        this.markIcon = markIcon;
        this.label = label;

        addActionListener(this::handleActionEvent);
        addHoverListener(createHoverListener());
        addKeyListener(createKeyListener());
        refreshVisualState();
    }

    /**
     * Indicates whether this checkbox is currently selected.
     *
     * @return {@code true} when selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Updates the selected state and notifies listeners when it changes.
     *
     * @param selected new selected state
     * @return current checkbox
     */
    public JFCheckBox setSelected(boolean selected) {
        return setSelected(selected, true);
    }

    /**
     * Returns the internal label component.
     *
     * @return label component
     */
    public JFText getLabel() {
        return label;
    }

    /**
     * Replaces the label text.
     *
     * @param text new label text
     * @return current checkbox
     */
    public JFCheckBox setText(String text) {
        label.setText(text == null ? "" : text);
        return this;
    }

    /**
     * Registers a listener invoked whenever the selected state changes.
     *
     * @param listener listener to add
     * @return current checkbox
     */
    public JFCheckBox addChangeListener(Consumer<JFCheckBox> listener) {
        if (listener == null) throw new IllegalArgumentException("Listener cannot be null");
        changeListeners.add(listener);
        return this;
    }

    /**
     * Removes a previously registered change listener.
     *
     * @param listener listener to remove
     * @return current checkbox
     */
    public JFCheckBox removeChangeListener(Consumer<JFCheckBox> listener) {
        changeListeners.remove(listener);
        return this;
    }

    @Override
    public JFCheckBox addHoverListener(JFHoverListener listener) {
        return (JFCheckBox) JFInteractiveComponent.super.addHoverListener(listener);
    }

    @Override
    public JFCheckBox addKeyListener(JFKeyListener listener) {
        return (JFCheckBox) JFInteractiveComponent.super.addKeyListener(listener);
    }

    /**
     * Handles press, release and click events emitted by the checkbox surface.
     *
     * @param event framework action event
     */
    private void handleActionEvent(JFActionEvent event) {
        if (event.getAction() == ActionEventTypes.DOWN) {
            pressed = true;
            refreshVisualState();
            return;
        }

        if (event.getAction() == ActionEventTypes.UP) {
            pressed = false;
            refreshVisualState();
            return;
        }

        if (event.getAction() == ActionEventTypes.CLICK) {
            pressed = false;
            setSelected(!selected, true);
        }
    }

    /**
     * Creates the internal hover listener that drives hover styling.
     *
     * @return hover listener bound to this checkbox
     */
    private JFHoverListener createHoverListener() {
        return event -> {
            hovered = event.getType() != HoverEventTypes.EXIT;
            refreshVisualState();
        };
    }

    /**
     * Creates the internal keyboard listener used for space/enter toggling.
     *
     * @return keyboard listener bound to this checkbox
     */
    private JFKeyListener createKeyListener() {
        return event -> {
            if (event.getType() != KeyEventTypes.KEY_PRESSED) {
                return;
            }

            if (event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_ENTER) {
                setSelected(!selected, true);
            }
        };
    }

    /**
     * Applies the new selected state and optionally notifies listeners.
     *
     * @param selected new selected state
     * @param notifyListeners whether listeners should be invoked
     * @return current checkbox
     */
    private JFCheckBox setSelected(boolean selected, boolean notifyListeners) {
        if (this.selected == selected) {
            refreshVisualState();
            return this;
        }

        this.selected = selected;
        refreshVisualState();

        if (notifyListeners) {
            for (Consumer<JFCheckBox> listener : List.copyOf(changeListeners)) {
                listener.accept(this);
            }
        }

        return this;
    }

    /**
     * Recomputes the visual styling from the current selected, hovered and pressed state.
     */
    private void refreshVisualState() {
        Color surfaceColor = SURFACE_COLOR;
        if (selected) {
            surfaceColor = pressed ? ACTIVE_PRESSED_COLOR : ACTIVE_COLOR;
        } else if (pressed) {
            surfaceColor = SURFACE_PRESSED_COLOR;
        } else if (hovered) {
            surfaceColor = SURFACE_HOVER_COLOR;
        }

        int borderThickness = hovered || selected ? 2 : 1;

        boxContainer.getDecoration()
                .setColor(surfaceColor)
                .setBorder(new Border(selected ? ACTIVE_PRESSED_COLOR.darker() : BORDER_COLOR, borderThickness));
        markIcon.setVisible(selected);
        markIcon.setColor(selected ? Color.WHITE : new Color(255, 255, 255, 0));
        label.setColor(LABEL_COLOR);
    }

    /**
     * Creates the text node used by the checkbox label.
     *
     * @param text label contents
     * @return configured label component
     */
    private static JFText createLabel(String text) {
        return new JFText(text == null ? "" : text)
                .setFont(LABEL_FONT)
                .setColor(LABEL_COLOR);
    }

    /**
     * Small checkmark glyph rendered inside the checkbox when selected.
     */
    private static final class CheckMarkIcon extends JFComponent {

        private Color color = Color.WHITE;

        private CheckMarkIcon(int size) {
            setSize(size, size);
        }

        private void setColor(Color color) {
            this.color = color;
        }

        @Override
        protected void layoutRecalculate() {
        }

        @Override
        protected void design(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            try {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int x = componentBox.x;
                int y = componentBox.y;
                int w = componentBox.width;
                int h = componentBox.height;

                g2d.drawLine(x + (w / 4), y + (h / 2), x + (w / 2) - 1, y + h - (h / 4));
                g2d.drawLine(x + (w / 2) - 1, y + h - (h / 4), x + w - (w / 5), y + (h / 4));
            } finally {
                g2d.dispose();
            }
        }
    }
}
