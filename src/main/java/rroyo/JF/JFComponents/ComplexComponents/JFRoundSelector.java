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
 * Radio-button style selector with optional mutual-exclusion grouping.
 * <p>
 * The component combines a circular indicator and a text label. It reacts to mouse and keyboard
 * input, exposes change listeners and can join a {@link Group} so that only one selector in the
 * group remains selected at a time.
 *
 * @author rroyo
 */
public class JFRoundSelector extends JFComplexComponent implements JFInteractiveComponent {

    private static final int CIRCLE_SIZE = 20;
    private static final int LABEL_GAP = 10;
    private static final Font LABEL_FONT = new Font("Dialog", Font.PLAIN, 13);
    private static final Color SURFACE_COLOR = new Color(250, 251, 252);
    private static final Color SURFACE_HOVER_COLOR = new Color(239, 244, 249);
    private static final Color SURFACE_PRESSED_COLOR = new Color(223, 232, 244);
    private static final Color BORDER_COLOR = new Color(71, 85, 105);
    private static final Color ACTIVE_COLOR = new Color(14, 165, 233);
    private static final Color ACTIVE_PRESSED_COLOR = new Color(2, 132, 199);
    private static final Color LABEL_COLOR = new Color(30, 41, 59);

    private final JFContainer circleContainer;
    private final RoundDotIcon dotIcon;
    private final JFText label;
    private final List<Consumer<JFRoundSelector>> changeListeners = new ArrayList<>();

    private boolean selected;
    private boolean hovered;
    private boolean pressed;
    private Group group;

    /**
     * Creates a selector with the provided label text.
     *
     * @param text label shown next to the round indicator
     */
    public JFRoundSelector(String text) {
        this(createLabel(text), new JFContainer(CIRCLE_SIZE, CIRCLE_SIZE, SURFACE_COLOR), new RoundDotIcon(CIRCLE_SIZE));
    }

    /**
     * Builds the selector from preconfigured internal nodes.
     *
     * @param label text node used for the label
     * @param circleContainer clickable circle surface
     * @param dotIcon icon used for the selected marker
     */
    private JFRoundSelector(JFText label, JFContainer circleContainer, RoundDotIcon dotIcon) {
        super(() -> {
            circleContainer.getDecoration()
                    .setBorder(new Border(BORDER_COLOR, 1))
                    .setBorderRadius(CIRCLE_SIZE / 2)
                    .setShadow(new BoxShadow(new Color(15, 23, 42, 30), 0, 2, 3));

            dotIcon.setPosition(0, 0);
            circleContainer.addChild(dotIcon);

            return new JFRow(
                    circleContainer,
                    new JFSizedBox(LABEL_GAP, 1),
                    label
            );
        });

        this.circleContainer = circleContainer;
        this.dotIcon = dotIcon;
        this.label = label;

        addActionListener(this::handleActionEvent);
        addHoverListener(createHoverListener());
        addKeyListener(createKeyListener());
        refreshVisualState();
    }

    /**
     * Indicates whether this selector is currently selected.
     *
     * @return {@code true} when selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Updates the selected state, delegating to the owning group when present.
     *
     * @param selected new selected state
     * @return current selector
     */
    public JFRoundSelector setSelected(boolean selected) {
        if (group != null && selected) {
            group.select(this);
            return this;
        }

        return updateSelectedState(selected, true);
    }

    /**
     * Attaches this selector to a group or removes it from the previous one.
     *
     * @param group new group, or {@code null} to detach
     * @return current selector
     */
    public JFRoundSelector setGroup(Group group) {
        if (this.group == group) {
            return this;
        }

        if (this.group != null) {
            this.group.members.remove(this);
        }

        this.group = group;

        if (group != null && !group.members.contains(this)) {
            group.members.add(this);
            if (selected) {
                group.select(this);
            }
        }

        return this;
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
     * @return current selector
     */
    public JFRoundSelector setText(String text) {
        label.setText(text == null ? "" : text);
        return this;
    }

    /**
     * Registers a listener invoked whenever the selected state changes.
     *
     * @param listener listener to add
     * @return current selector
     */
    public JFRoundSelector addChangeListener(Consumer<JFRoundSelector> listener) {
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
    public JFRoundSelector removeChangeListener(Consumer<JFRoundSelector> listener) {
        changeListeners.remove(listener);
        return this;
    }

    @Override
    public JFRoundSelector addHoverListener(JFHoverListener listener) {
        return (JFRoundSelector) JFInteractiveComponent.super.addHoverListener(listener);
    }

    @Override
    public JFRoundSelector addKeyListener(JFKeyListener listener) {
        return (JFRoundSelector) JFInteractiveComponent.super.addKeyListener(listener);
    }

    /**
     * Handles press, release and click events emitted by the selector surface.
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
            if (group != null) {
                group.select(this);
            } else {
                updateSelectedState(!selected, true);
            }
        }
    }

    /**
     * Creates the internal hover listener that updates hover state styling.
     *
     * @return hover listener bound to this selector
     */
    private JFHoverListener createHoverListener() {
        return event -> {
            hovered = event.getType() != HoverEventTypes.EXIT;
            refreshVisualState();
        };
    }

    /**
     * Creates the internal keyboard listener used for space/enter activation.
     *
     * @return keyboard listener bound to this selector
     */
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
     * Applies the new selected state and optionally notifies listeners.
     *
     * @param selected new selected state
     * @param notifyListeners whether change listeners should be invoked
     * @return current selector
     */
    private JFRoundSelector updateSelectedState(boolean selected, boolean notifyListeners) {
        if (this.selected == selected) {
            refreshVisualState();
            return this;
        }

        this.selected = selected;
        refreshVisualState();

        if (notifyListeners) {
            notifyChangeListeners();
        }

        return this;
    }

    /**
     * Notifies registered listeners using a snapshot to avoid concurrent modification issues.
     */
    private void notifyChangeListeners() {
        for (Consumer<JFRoundSelector> listener : List.copyOf(changeListeners)) {
            listener.accept(this);
        }
    }

    /**
     * Recomputes the surface, border and marker colors from the current interaction state.
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

        circleContainer.getDecoration()
                .setColor(surfaceColor)
                .setBorder(new Border(selected ? ACTIVE_PRESSED_COLOR.darker() : BORDER_COLOR, borderThickness));
        dotIcon.setVisible(selected);
        dotIcon.setColor(selected ? Color.WHITE : new Color(255, 255, 255, 0));
        label.setColor(LABEL_COLOR);
    }

    /**
     * Creates the text node used by the selector label.
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
     * Logical radio group that keeps exactly one member selected at a time.
     */
    public static final class Group {

        private final List<JFRoundSelector> members = new ArrayList<>();

        /**
         * Adds one or more selectors to this group.
         *
         * @param selectors selectors to associate with the group
         * @return current group
         */
        public Group add(JFRoundSelector... selectors) {
            if (selectors == null) {
                return this;
            }

            for (JFRoundSelector selector : selectors) {
                if (selector != null) {
                    selector.setGroup(this);
                }
            }
            return this;
        }

        /**
         * Selects the given member and unselects every other group member.
         *
         * @param selectedSelector member that should remain selected
         */
        private void select(JFRoundSelector selectedSelector) {
            for (JFRoundSelector member : List.copyOf(members)) {
                member.updateSelectedState(member == selectedSelector, true);
            }
        }
    }

    /**
     * Small filled-circle glyph used as the selected-state indicator.
     */
    private static final class RoundDotIcon extends JFComponent {

        private Color color = Color.WHITE;

        private RoundDotIcon(int size) {
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

                int size = Math.min(componentBox.width, componentBox.height) / 2;
                int x = componentBox.x + ((componentBox.width - size) / 2);
                int y = componentBox.y + ((componentBox.height - size) / 2);
                g2d.fillOval(x, y, size, size);
            } finally {
                g2d.dispose();
            }
        }
    }
}
