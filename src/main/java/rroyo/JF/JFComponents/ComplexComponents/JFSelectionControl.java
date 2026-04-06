package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.Decorations.Border;
import rroyo.JF.Decorations.BoxShadow;
import rroyo.JF.Enums.ActionEventTypes;
import rroyo.JF.Enums.HoverEventTypes;
import rroyo.JF.Enums.KeyEventTypes;
import rroyo.JF.Enums.SelectionType;
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
 * Unified selection control that can behave as either a checkbox or a round selector.
 * <p>
 * The component encapsulates the shared interaction logic of both widgets and changes its
 * visuals according to the requested {@link SelectionType}. When attached to a {@link JFGroup},
 * selection becomes mutually exclusive.
 *
 * @author rroyo
 */
public class JFSelectionControl extends JFComplexComponent implements JFInteractiveComponent {

    private static final int INDICATOR_SIZE = 20;
    private static final int LABEL_GAP = 10;
    private static final Font LABEL_FONT = new Font("Dialog", Font.PLAIN, 13);
    private static final Color LABEL_COLOR = new Color(30, 41, 59);

    private final SelectionType type;
    private final SelectionTheme theme;
    private final JFContainer indicatorContainer;
    private final SelectionIcon indicatorIcon;
    private final JFText label;
    private final List<Consumer<JFSelectionControl>> changeListeners = new ArrayList<>();

    private boolean selected;
    private boolean hovered;
    private boolean pressed;
    private JFGroup group;

    /**
     * Creates a selection control with the requested visual type and label text.
     *
     * @param type desired selector type
     * @param text label shown next to the indicator
     */
    public JFSelectionControl(SelectionType type, String text) {
        this(type, createLabel(text), new JFContainer(INDICATOR_SIZE, INDICATOR_SIZE, getTheme(type).surfaceColor()), new SelectionIcon(type, INDICATOR_SIZE));
    }

    private JFSelectionControl(SelectionType type, JFText label, JFContainer indicatorContainer, SelectionIcon indicatorIcon) {
        super(() -> {
            SelectionTheme theme = getTheme(type);
            indicatorContainer.getDecoration()
                    .setBorder(new Border(theme.borderColor(), 1))
                    .setBorderRadius(theme.borderRadius())
                    .setShadow(new BoxShadow(theme.shadowColor(), 0, 2, 3));

            indicatorIcon.setPosition(0, 0);
            indicatorContainer.addChild(indicatorIcon);

            return new JFRow(
                    indicatorContainer,
                    new JFSizedBox(LABEL_GAP, 1),
                    label
            );
        });

        this.type = type;
        this.theme = getTheme(type);
        this.indicatorContainer = indicatorContainer;
        this.indicatorIcon = indicatorIcon;
        this.label = label;

        addActionListener(this::handleActionEvent);
        addHoverListener(createHoverListener());
        addKeyListener(createKeyListener());
        refreshVisualState();
    }

    /**
     * Returns the visual type of this selector.
     *
     * @return configured selection type
     */
    public SelectionType getType() {
        return type;
    }

    /**
     * Indicates whether the control is currently selected.
     *
     * @return {@code true} when selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Updates the selected state, delegating to the current group when needed.
     *
     * @param selected new selected state
     * @return current selector
     */
    public JFSelectionControl setSelected(boolean selected) {
        if (group != null && selected) {
            group.select(this);
            return this;
        }

        return updateSelectedState(selected, true);
    }

    /**
     * Attaches this selector to a group or detaches it from the previous one.
     *
     * @param group target group, or {@code null} to detach
     * @return current selector
     */
    public JFSelectionControl setGroup(JFGroup group) {
        if (this.group == group) {
            return this;
        }

        if (this.group != null) {
            this.group.detachMember(this);
        }

        this.group = group;

        if (group != null) {
            group.attachMember(this);
            if (selected) {
                group.select(this);
            }
        }

        return this;
    }

    /**
     * Returns the current group, or {@code null} when detached.
     *
     * @return owning group, or {@code null}
     */
    public JFGroup getGroup() {
        return group;
    }

    /**
     * Returns the internal label component for further styling.
     *
     * @return label component
     */
    public JFText getLabel() {
        return label;
    }

    /**
     * Replaces the visible label text.
     *
     * @param text new label text
     * @return current selector
     */
    public JFSelectionControl setText(String text) {
        label.setText(text == null ? "" : text);
        return this;
    }

    /**
     * Registers a listener invoked whenever the selected state changes.
     *
     * @param listener listener to add
     * @return current selector
     */
    public JFSelectionControl addChangeListener(Consumer<JFSelectionControl> listener) {
        if (listener == null) throw new IllegalArgumentException("Listener cannot be null");
        changeListeners.add(listener);
        return this;
    }

    /**
     * Removes a previously registered change listener.
     *
     * @param listener listener to remove
     * @return current selector
     */
    public JFSelectionControl removeChangeListener(Consumer<JFSelectionControl> listener) {
        changeListeners.remove(listener);
        return this;
    }

    @Override
    public JFSelectionControl addHoverListener(JFHoverListener listener) {
        return (JFSelectionControl) JFInteractiveComponent.super.addHoverListener(listener);
    }

    @Override
    public JFSelectionControl addKeyListener(JFKeyListener listener) {
        return (JFSelectionControl) JFInteractiveComponent.super.addKeyListener(listener);
    }

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
            if (group != null) {
                group.select(this);
            } else {
                updateSelectedState(!selected, true);
            }
        }
    }

    private JFHoverListener createHoverListener() {
        return event -> {
            hovered = event.getType() != HoverEventTypes.EXIT;
            refreshVisualState();
        };
    }

    private JFKeyListener createKeyListener() {
        return event -> {
            if (event.getType() != KeyEventTypes.KEY_PRESSED) {
                return;
            }

            if (event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_ENTER) {
                if (group != null) {
                    group.select(this);
                } else {
                    updateSelectedState(!selected, true);
                }
            }
        };
    }

    /**
     * Applies a selected-state change from the control itself or from a group.
     *
     * @param selected new selected state
     * @param notifyListeners whether listeners should be notified
     * @return current selector
     */
    JFSelectionControl updateSelectedState(boolean selected, boolean notifyListeners) {
        if (this.selected == selected) {
            refreshVisualState();
            return this;
        }

        this.selected = selected;
        refreshVisualState();

        if (notifyListeners) {
            for (Consumer<JFSelectionControl> listener : List.copyOf(changeListeners)) {
                listener.accept(this);
            }
        }

        return this;
    }

    private void refreshVisualState() {
        Color surfaceColor = theme.surfaceColor();
        if (selected) {
            surfaceColor = pressed ? theme.activePressedColor() : theme.activeColor();
        } else if (pressed) {
            surfaceColor = theme.surfacePressedColor();
        } else if (hovered) {
            surfaceColor = theme.surfaceHoverColor();
        }

        int borderThickness = hovered || selected ? 2 : 1;

        indicatorContainer.getDecoration()
                .setColor(surfaceColor)
                .setBorder(new Border(selected ? theme.activePressedColor().darker() : theme.borderColor(), borderThickness));
        indicatorIcon.setVisible(selected);
        indicatorIcon.setColor(selected ? Color.WHITE : new Color(255, 255, 255, 0));
        label.setColor(LABEL_COLOR);
    }

    private static JFText createLabel(String text) {
        return new JFText(text == null ? "" : text)
                .setFont(LABEL_FONT)
                .setColor(LABEL_COLOR);
    }

    private static SelectionTheme getTheme(SelectionType type) {
        return switch (type) {
            case CHECKBOX -> new SelectionTheme(
                    new Color(245, 247, 250),
                    new Color(236, 241, 247),
                    new Color(222, 231, 241),
                    new Color(71, 85, 105),
                    new Color(37, 99, 235),
                    new Color(29, 78, 216),
                    new Color(15, 23, 42, 35),
                    6
            );
            case ROUND -> new SelectionTheme(
                    new Color(250, 251, 252),
                    new Color(239, 244, 249),
                    new Color(223, 232, 244),
                    new Color(71, 85, 105),
                    new Color(14, 165, 233),
                    new Color(2, 132, 199),
                    new Color(15, 23, 42, 30),
                    INDICATOR_SIZE / 2
            );
        };
    }

    private record SelectionTheme(
            Color surfaceColor,
            Color surfaceHoverColor,
            Color surfacePressedColor,
            Color borderColor,
            Color activeColor,
            Color activePressedColor,
            Color shadowColor,
            int borderRadius
    ) {
    }

    /**
     * Draws either the checkbox mark or the round inner dot depending on the selector type.
     */
    private static final class SelectionIcon extends JFComponent {

        private final SelectionType type;
        private Color color = Color.WHITE;

        private SelectionIcon(SelectionType type, int size) {
            this.type = type;
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

                if (type == SelectionType.CHECKBOX) {
                    g2d.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    int x = componentBox.x;
                    int y = componentBox.y;
                    int w = componentBox.width;
                    int h = componentBox.height;
                    g2d.drawLine(x + (w / 4), y + (h / 2), x + (w / 2) - 1, y + h - (h / 4));
                    g2d.drawLine(x + (w / 2) - 1, y + h - (h / 4), x + w - (w / 5), y + (h / 4));
                } else {
                    int size = Math.min(componentBox.width, componentBox.height) / 2;
                    int x = componentBox.x + ((componentBox.width - size) / 2);
                    int y = componentBox.y + ((componentBox.height - size) / 2);
                    g2d.fillOval(x, y, size, size);
                }
            } finally {
                g2d.dispose();
            }
        }
    }
}
