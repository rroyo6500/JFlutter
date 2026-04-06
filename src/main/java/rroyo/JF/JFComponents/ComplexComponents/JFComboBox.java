package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.Decorations.Border;
import rroyo.JF.Decorations.BoxShadow;
import rroyo.JF.Enums.Alignment;
import rroyo.JF.Enums.ActionEventTypes;
import rroyo.JF.Enums.HoverEventTypes;
import rroyo.JF.Enums.KeyEventTypes;
import rroyo.JF.JFComponents.BaseComponent.JFComplexComponent;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFContainer;
import rroyo.JF.JFComponents.SimpleComponents.JFRow;
import rroyo.JF.JFComponents.SimpleComponents.JFScrollColumn;
import rroyo.JF.JFComponents.SimpleComponents.JFStack;
import rroyo.JF.JFComponents.SimpleComponents.JFText;
import rroyo.JF.JFComponents.SimpleComponents.JFViewport;
import rroyo.JF.JFComponents.SimpleComponents.JFWindow;
import rroyo.JF.JFEvents.JFActionEvent;
import rroyo.JF.JFEvents.JFFocusTargetComponent;
import rroyo.JF.JFEvents.JFHoverEvent;
import rroyo.JF.JFEvents.JFInteractiveComponent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Interactive combo box with keyboard navigation, hover feedback and scrollable options.
 * <p>
 * The widget is composed from framework primitives: a clickable header, a floating dropdown,
 * a clipped viewport for the option list and a scrollbar that appears when the list exceeds the
 * visible height. Selection can be changed with the mouse wheel, arrow keys and direct clicks.
 *
 * @author rroyo
 */
public class JFComboBox extends JFComplexComponent implements JFInteractiveComponent, JFFocusTargetComponent {

    private static final int DEFAULT_WIDTH = 220;
    private static final int DEFAULT_ITEM_HEIGHT = 34;
    private static final int TEXT_PADDING_X = 12;
    private static final int DEFAULT_MAX_VISIBLE_ITEMS = 6;
    private static final int SCROLL_BAR_WIDTH = 12;
    private static final Font TEXT_FONT = new Font("Dialog", Font.PLAIN, 13);
    private static final Color HEADER_COLOR = new Color(248, 250, 252);
    private static final Color HEADER_HOVER_COLOR = new Color(241, 245, 249);
    private static final Color HEADER_PRESSED_COLOR = new Color(226, 232, 240);
    private static final Color HEADER_EXPANDED_COLOR = new Color(232, 240, 255);
    private static final Color OPTION_COLOR = new Color(255, 255, 255);
    private static final Color OPTION_HOVER_COLOR = new Color(239, 246, 255);
    private static final Color OPTION_SELECTED_COLOR = new Color(219, 234, 254);
    private static final Color BORDER_COLOR = new Color(100, 116, 139);
    private static final Color ACCENT_COLOR = new Color(37, 99, 235);
    private static final Color TEXT_COLOR = new Color(15, 23, 42);

    private final int width;
    private final int itemHeight;
    private final HeaderButton headerButton;
    private final JFContainer dropdownContainer;
    private final JFViewport dropdownViewport;
    private final JFScrollBar scrollBar;
    private final JFText selectedText;
    private final ArrowIcon arrowIcon;
    private final List<String> options = new ArrayList<>();
    private final List<OptionItem> optionItems = new ArrayList<>();
    private final List<Consumer<JFComboBox>> selectionListeners = new ArrayList<>();

    private JFScrollColumn optionsColumn;
    private int selectedIndex = -1;
    private boolean expanded;

    /**
     * Creates a combo box with the default size and no options.
     */
    public JFComboBox() {
        this(DEFAULT_WIDTH, DEFAULT_ITEM_HEIGHT);
    }

    /**
     * Creates an empty combo box with explicit dimensions.
     *
     * @param width combo-box width in pixels
     * @param itemHeight height of the header and each option row
     */
    public JFComboBox(int width, int itemHeight) {
        this(width, itemHeight, buildStructure(width, itemHeight));
    }

    /**
     * Creates a combo box with default dimensions and an initial option list.
     *
     * @param options initial options to display
     */
    public JFComboBox(String... options) {
        this(DEFAULT_WIDTH, DEFAULT_ITEM_HEIGHT, options);
    }

    /**
     * Creates a combo box with explicit dimensions and an initial option list.
     *
     * @param width combo-box width in pixels
     * @param itemHeight height of the header and each option row
     * @param options initial options to display
     */
    public JFComboBox(int width, int itemHeight, String... options) {
        this(width, itemHeight);
        setOptions(options);
    }

    /**
     * Builds the widget from the preconstructed internal nodes used by the component.
     *
     * @param width combo-box width in pixels
     * @param itemHeight height of the header and each option row
     * @param structure internal node bundle used by the component
     */
    private JFComboBox(int width, int itemHeight, ComboStructure structure) {
        super(() -> {
            JFStack rootStack = new JFStack(Alignment.CUSTOM);
            rootStack.addChilds(structure.headerButton(), structure.dropdownContainer());
            return new JFInteractuable(new JFSizedStack(Alignment.CUSTOM, width, itemHeight).addChild(rootStack));
        });

        this.width = width;
        this.itemHeight = itemHeight;
        this.headerButton = structure.headerButton();
        this.dropdownContainer = structure.dropdownContainer();
        this.dropdownViewport = structure.dropdownViewport();
        this.scrollBar = structure.scrollBar();
        this.optionsColumn = structure.optionsColumn();
        this.selectedText = structure.selectedText();
        this.arrowIcon = structure.arrowIcon();

        this.headerButton.addActionListener(this::handleHeaderAction);
        this.headerButton.addHoverListener(this::handleHeaderHover);
        this.scrollBar.addValueChangeListener(value -> dropdownViewport.setScrollY(value));
        addWheelListener(event -> {
            if (!expanded || !dropdownContainer.containsPoint(event.getMouseX(), event.getMouseY())) {
                return;
            }

            int scrollStep = Math.max(1, itemHeight / 2);
            int nextValue = scrollBar.getValue() + (event.getWheelRotation() * scrollStep);
            scrollBar.setValue(nextValue);
            refreshVisualState();
        });

        addKeyListener(event -> {
            if (event.getType() != KeyEventTypes.KEY_PRESSED) {
                return;
            }

            switch (event.getKeyCode()) {
                case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> setExpanded(!expanded);
                case KeyEvent.VK_ESCAPE -> setExpanded(false);
                case KeyEvent.VK_DOWN -> moveSelection(1);
                case KeyEvent.VK_UP -> moveSelection(-1);
            }
        });

        refreshVisualState();
    }

    /**
     * Creates the internal header, dropdown viewport and scrollbar used by the combo box.
     *
     * @param width combo-box width in pixels
     * @param itemHeight height of the header and each option row
     * @return grouped structure containing the internal visual nodes
     */
    private static ComboStructure buildStructure(int width, int itemHeight) {
        JFText selectedText = new JFText("").setFont(TEXT_FONT).setColor(TEXT_COLOR);
        ArrowIcon arrowIcon = new ArrowIcon(18, 18);
        JFContainer headerSurface = new JFContainer(width, itemHeight, HEADER_COLOR);
        JFContainer dropdownContainer = new JFContainer(width, 0, OPTION_COLOR);
        JFViewport dropdownViewport = new JFViewport(width, 0);
        JFScrollBar scrollBar = new JFScrollBar(SCROLL_BAR_WIDTH, 0);
        JFScrollColumn optionsColumn = new JFScrollColumn();
        HeaderButton headerButton = new HeaderButton(headerSurface);

        selectedText.setPosition(TEXT_PADDING_X, calculateTextY(itemHeight));
        arrowIcon.setPosition(width - arrowIcon.getWidth() - TEXT_PADDING_X, (itemHeight - arrowIcon.getHeight()) / 2);

        headerSurface.getDecoration()
                .setBorder(new Border(BORDER_COLOR, 1))
                .setBorderRadius(12)
                .setShadow(new BoxShadow(new Color(15, 23, 42, 28), 0, 3, 4));
        headerSurface.addChild(new JFStack(Alignment.CUSTOM).addChilds(selectedText, arrowIcon));

        dropdownContainer.getDecoration()
                .setBorder(new Border(BORDER_COLOR, 1))
                .setBorderRadius(12)
                .setShadow(new BoxShadow(new Color(15, 23, 42, 24), 0, 4, 5));
        dropdownContainer.setPosition(0, itemHeight);
        dropdownContainer.setOverflowAllowed(true);

        dropdownViewport.addChild(optionsColumn);
        scrollBar.setVisible(false).setActive(false);
        dropdownContainer.addChild(new JFRow(dropdownViewport, scrollBar));

        return new ComboStructure(selectedText, arrowIcon, headerButton, dropdownContainer, dropdownViewport, scrollBar, optionsColumn);
    }

    /**
     * Returns an immutable snapshot of the current option labels.
     *
     * @return current option list
     */
    public List<String> getOptions() {
        return List.copyOf(options);
    }

    /**
     * Appends an option to the end of the list.
     *
     * @param option option label to add
     * @return current combo box
     */
    public JFComboBox addOption(String option) {
        options.add(option == null ? "" : option);
        rebuildOptions();
        if (selectedIndex < 0) {
            selectedIndex = 0;
        }
        refreshVisualState();
        return this;
    }

    /**
     * Replaces the full option list and adjusts the selection if needed.
     *
     * @param options new option labels
     * @return current combo box
     */
    public JFComboBox setOptions(String... options) {
        this.options.clear();
        if (options != null) {
            this.options.addAll(Arrays.asList(options));
        }

        rebuildOptions();
        if (this.options.isEmpty()) {
            selectedIndex = -1;
        } else if (selectedIndex < 0 || selectedIndex >= this.options.size()) {
            selectedIndex = 0;
        }

        refreshVisualState();
        return this;
    }

    /**
     * Indicates whether the dropdown is currently open.
     *
     * @return {@code true} when the options list is expanded
     */
    public boolean isExpanded() {
        return expanded;
    }

    /**
     * Opens or closes the dropdown and refreshes the overlay state.
     *
     * @param expanded new expansion state
     * @return current combo box
     */
    public JFComboBox setExpanded(boolean expanded) {
        if (this.expanded == expanded) {
            refreshVisualState();
            return this;
        }

        this.expanded = expanded;
        refreshVisualState();
        return this;
    }

    /**
     * Returns the index of the currently selected option.
     *
     * @return selected option index, or {@code -1} when nothing is selected
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Returns the currently selected option label.
     *
     * @return selected option label, or {@code null} when nothing is selected
     */
    public String getSelectedItem() {
        return selectedIndex >= 0 && selectedIndex < options.size() ? options.get(selectedIndex) : null;
    }

    /**
     * Selects an option and notifies listeners when the value changes.
     *
     * @param index index to select
     * @return current combo box
     */
    public JFComboBox setSelectedIndex(int index) {
        return setSelectedIndex(index, true);
    }

    /**
     * Registers a listener invoked after the selection changes.
     *
     * @param listener listener to add
     * @return current combo box
     */
    public JFComboBox addSelectionListener(Consumer<JFComboBox> listener) {
        if (listener == null) throw new IllegalArgumentException("Listener cannot be null");
        selectionListeners.add(listener);
        return this;
    }

    /**
     * Removes a previously registered selection listener.
     *
     * @param listener listener to remove
     * @return current combo box
     */
    public JFComboBox removeSelectionListener(Consumer<JFComboBox> listener) {
        selectionListeners.remove(listener);
        return this;
    }

    @Override
    public JFComponent getKeyFocusTarget() {
        return this;
    }

    /**
     * Updates the header state in response to press, release and click actions.
     *
     * @param event framework action event coming from the header button
     */
    private void handleHeaderAction(JFActionEvent event) {
        if (event.getAction() == ActionEventTypes.DOWN) {
            headerButton.setPressed(true);
            refreshVisualState();
            return;
        }

        if (event.getAction() == ActionEventTypes.UP) {
            headerButton.setPressed(false);
            refreshVisualState();
            return;
        }

        if (event.getAction() == ActionEventTypes.CLICK) {
            headerButton.setPressed(false);
            setExpanded(!expanded);
        }
    }

    /**
     * Tracks header hover state so the visual styling can react to pointer movement.
     *
     * @param event hover event emitted by the header
     */
    private void handleHeaderHover(JFHoverEvent event) {
        headerButton.setHovered(event.getType() != HoverEventTypes.EXIT);
        refreshVisualState();
    }

    /**
     * Moves the current selection up or down while keeping the dropdown open.
     *
     * @param delta signed step applied to the current selection
     */
    private void moveSelection(int delta) {
        if (options.isEmpty()) {
            return;
        }

        int currentIndex = selectedIndex < 0 ? 0 : selectedIndex;
        int nextIndex = Math.max(0, Math.min(options.size() - 1, currentIndex + delta));
        setSelectedIndex(nextIndex, true);
        setExpanded(true);
        ensureSelectionIsVisible();
    }

    /**
     * Applies a new selected index and optionally notifies registered listeners.
     *
     * @param index requested index
     * @param notifyListeners whether listeners should be called after the change
     * @return current combo box
     */
    private JFComboBox setSelectedIndex(int index, boolean notifyListeners) {
        if (options.isEmpty()) {
            selectedIndex = -1;
            refreshVisualState();
            return this;
        }

        int boundedIndex = Math.max(0, Math.min(index, options.size() - 1));
        if (selectedIndex == boundedIndex) {
            refreshVisualState();
            return this;
        }

        selectedIndex = boundedIndex;
        refreshVisualState();
        ensureSelectionIsVisible();

        if (notifyListeners) {
            for (Consumer<JFComboBox> listener : List.copyOf(selectionListeners)) {
                listener.accept(this);
            }
        }

        return this;
    }

    /**
     * Rebuilds the option-item components to match the current option labels.
     */
    private void rebuildOptions() {
        optionsColumn = new JFScrollColumn();
        dropdownViewport.addChild(optionsColumn);
        optionItems.clear();

        for (int i = 0; i < options.size(); i++) {
            OptionItem optionItem = new OptionItem(width, itemHeight, options.get(i), i);
            optionItem.addActionListener(event -> {
                if (event.getAction() == ActionEventTypes.CLICK) {
                    setSelectedIndex(optionItem.optionIndex, true);
                    setExpanded(false);
                } else if (event.getAction() == ActionEventTypes.DOWN) {
                    optionItem.setPressed(true);
                    refreshVisualState();
                } else if (event.getAction() == ActionEventTypes.UP) {
                    optionItem.setPressed(false);
                    refreshVisualState();
                }
            });
            optionItem.addHoverListener(event -> {
                optionItem.setHovered(event.getType() != HoverEventTypes.EXIT);
                refreshVisualState();
            });

            optionItems.add(optionItem);
            optionsColumn.addChild(optionItem);
        }
    }

    /**
     * Synchronizes header, dropdown, scrollbar and option-row visuals with the current state.
     */
    private void refreshVisualState() {
        String text = getSelectedItem();
        selectedText.setText(text == null ? "Selecciona una opcion" : text);

        Color headerColor = expanded ? HEADER_EXPANDED_COLOR : HEADER_COLOR;
        if (headerButton.isPressed()) {
            headerColor = HEADER_PRESSED_COLOR;
        } else if (headerButton.isHovered()) {
            headerColor = expanded ? HEADER_EXPANDED_COLOR.brighter() : HEADER_HOVER_COLOR;
        }

        headerButton.surface.getDecoration()
                .setColor(headerColor)
                .setBorder(new Border(expanded ? ACCENT_COLOR : BORDER_COLOR, expanded || headerButton.isHovered() ? 2 : 1));

        arrowIcon.setExpanded(expanded);

        int contentHeight = optionItems.size() * itemHeight;
        int dropdownHeight = expanded ? calculateVisibleDropdownHeight(contentHeight) : 0;
        boolean showScrollBar = contentHeight > dropdownHeight && dropdownHeight > 0;
        int viewportWidth = getViewportWidth(showScrollBar);

        dropdownContainer.setPosition(0, calculateDropdownY(dropdownHeight));
        dropdownContainer.setSize(width, dropdownHeight);
        dropdownContainer.setActive(expanded).setVisible(expanded);

        dropdownViewport.setSize(viewportWidth, dropdownHeight);
        dropdownViewport.setScrollY(Math.min(dropdownViewport.getScrollY(), Math.max(0, contentHeight - dropdownHeight)));
        optionsColumn.setSize(viewportWidth, contentHeight);

        scrollBar.setPosition(width - SCROLL_BAR_WIDTH, 0);
        scrollBar.setSize(SCROLL_BAR_WIDTH, dropdownHeight);
        scrollBar.setRange(dropdownHeight, contentHeight);
        scrollBar.setValue(dropdownViewport.getScrollY());
        scrollBar.setActive(showScrollBar).setVisible(showScrollBar);

        syncOverlayState();

        for (OptionItem optionItem : optionItems) {
            boolean isSelected = optionItem.optionIndex == selectedIndex;
            optionItem.setSize(viewportWidth, itemHeight);
            optionItem.refreshVisualState(isSelected);
        }
    }

    /**
     * Scrolls the dropdown so the selected option remains visible inside the viewport.
     */
    private void ensureSelectionIsVisible() {
        if (selectedIndex < 0 || !expanded || dropdownViewport.getHeight() <= 0) {
            return;
        }

        int selectedTop = selectedIndex * itemHeight;
        int selectedBottom = selectedTop + itemHeight;
        int currentTop = dropdownViewport.getScrollY();
        int currentBottom = currentTop + dropdownViewport.getHeight();

        if (selectedTop < currentTop) {
            scrollBar.setValue(selectedTop);
        } else if (selectedBottom > currentBottom) {
            scrollBar.setValue(selectedBottom - dropdownViewport.getHeight());
        }
    }

    /**
     * Registers or removes the dropdown from the window overlay layer depending on expansion state.
     */
    private void syncOverlayState() {
        JFWindow window = findWindowAncestor();
        if (window == null) {
            return;
        }

        if (expanded) {
            window.registerOverlay(dropdownContainer);
        } else {
            window.unregisterOverlay(dropdownContainer);
        }
    }

    /**
     * Walks up the tree to locate the owning window.
     *
     * @return nearest ancestor window, or {@code null} when detached
     */
    private JFWindow findWindowAncestor() {
        JFComponent current = this;
        while (current != null) {
            if (current instanceof JFWindow window) {
                return window;
            }
            current = current.getParent();
        }
        return null;
    }

    /**
     * Chooses the dropdown height that best fits the available space around the combo box.
     *
     * @param contentHeight total height of all options
     * @return visible dropdown height in pixels
     */
    private int calculateVisibleDropdownHeight(int contentHeight) {
        if (contentHeight <= 0) {
            return 0;
        }

        int preferredHeight = Math.min(contentHeight, itemHeight * DEFAULT_MAX_VISIBLE_ITEMS);
        JFWindow window = findWindowAncestor();
        if (window == null) {
            return preferredHeight;
        }

        int comboTop = getComponentBox().y;
        int spaceBelow = Math.max(itemHeight, window.getHeight() - (comboTop + itemHeight));
        int spaceAbove = Math.max(itemHeight, comboTop);
        int bestSpace = Math.max(spaceBelow, spaceAbove);

        return Math.min(contentHeight, Math.min(preferredHeight, bestSpace));
    }

    /**
     * Chooses whether the dropdown should open below or above the header.
     *
     * @param dropdownHeight computed dropdown height
     * @return local Y position for the dropdown container
     */
    private int calculateDropdownY(int dropdownHeight) {
        JFWindow window = findWindowAncestor();
        if (window == null || dropdownHeight <= 0) {
            return itemHeight;
        }

        int comboTop = getComponentBox().y;
        int spaceBelow = window.getHeight() - (comboTop + itemHeight);
        int spaceAbove = comboTop;
        return (spaceBelow >= dropdownHeight || spaceBelow >= spaceAbove) ? itemHeight : -dropdownHeight;
    }

    /**
     * Returns the width available for the viewport after reserving scrollbar space when needed.
     *
     * @param showScrollBar whether the scrollbar is currently visible
     * @return viewport width in pixels
     */
    private int getViewportWidth(boolean showScrollBar) {
        return width - (showScrollBar ? SCROLL_BAR_WIDTH : 0);
    }

    /**
     * Computes the Y offset used to vertically center text within one option row.
     *
     * @param itemHeight row height in pixels
     * @return local Y position for text
     */
    private static int calculateTextY(int itemHeight) {
        FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(TEXT_FONT);
        return Math.max(0, (itemHeight - metrics.getHeight()) / 2);
    }

    /**
     * Small interactive wrapper representing the combo-box header surface.
     */
    private static final class HeaderButton extends JFComplexComponent implements JFInteractiveComponent {

        private final JFContainer surface;
        private boolean hovered;
        private boolean pressed;

        private HeaderButton(JFContainer surface) {
            super(() -> surface);
            this.surface = surface;
        }

        private boolean isHovered() {
            return hovered;
        }

        private void setHovered(boolean hovered) {
            this.hovered = hovered;
        }

        private boolean isPressed() {
            return pressed;
        }

        private void setPressed(boolean pressed) {
            this.pressed = pressed;
        }
    }

    /**
     * Immutable bundle used while assembling the internal combo-box composition.
     */
    private record ComboStructure(
            JFText selectedText,
            ArrowIcon arrowIcon,
            HeaderButton headerButton,
            JFContainer dropdownContainer,
            JFViewport dropdownViewport,
            JFScrollBar scrollBar,
            JFScrollColumn optionsColumn
    ) {
    }

    /**
     * Interactive row representing a single selectable option in the dropdown.
     */
    private final class OptionItem extends JFComplexComponent implements JFInteractiveComponent {

        private final int optionIndex;
        private final JFContainer surface;
        private final JFText text;
        private boolean hovered;
        private boolean pressed;

        /**
         * Creates an option row from a label string.
         *
         * @param width row width
         * @param itemHeight row height
         * @param label option label
         * @param optionIndex index represented by this row
         */
        private OptionItem(int width, int itemHeight, String label, int optionIndex) {
            this(
                    optionIndex,
                    new JFText(label == null ? "" : label).setFont(TEXT_FONT).setColor(TEXT_COLOR),
                    new JFContainer(width, itemHeight, OPTION_COLOR),
                    itemHeight
            );
        }

        /**
         * Creates an option row from prebuilt internal nodes.
         *
         * @param optionIndex index represented by this row
         * @param text text node used for the label
         * @param surface clickable row surface
         * @param itemHeight row height
         */
        private OptionItem(int optionIndex, JFText text, JFContainer surface, int itemHeight) {
            super(() -> {
                surface.getDecoration().setColor(OPTION_COLOR);
                text.setPosition(TEXT_PADDING_X, calculateTextY(itemHeight));
                surface.addChild(text);
                return surface;
            });

            this.optionIndex = optionIndex;
            this.surface = surface;
            this.text = text;
        }

        /**
         * Updates the row hover flag.
         *
         * @param hovered whether the pointer is currently over this row
         */
        private void setHovered(boolean hovered) {
            this.hovered = hovered;
        }

        /**
         * Updates the row pressed flag.
         *
         * @param pressed whether the row is currently pressed
         */
        private void setPressed(boolean pressed) {
            this.pressed = pressed;
        }

        /**
         * Applies the current selection, hover and pressed colors to this option row.
         *
         * @param selected whether this row represents the selected option
         */
        private void refreshVisualState(boolean selected) {
            Color color = OPTION_COLOR;
            if (selected) {
                color = OPTION_SELECTED_COLOR;
            } else if (pressed) {
                color = OPTION_HOVER_COLOR.darker();
            } else if (hovered) {
                color = OPTION_HOVER_COLOR;
            }

            surface.getDecoration().setColor(color);
            text.setColor(TEXT_COLOR);
        }
    }

    /**
     * Lightweight arrow glyph used by the combo-box header to show expansion direction.
     */
    private static final class ArrowIcon extends JFComponent {

        private boolean expanded;

        private ArrowIcon(int width, int height) {
            setSize(width, height);
        }

        private void setExpanded(boolean expanded) {
            this.expanded = expanded;
        }

        @Override
        protected void layoutRecalculate() {
        }

        @Override
        protected void design(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            try {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(BORDER_COLOR.darker());
                g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int leftX = componentBox.x + 4;
                int rightX = componentBox.x + componentBox.width - 4;
                int centerX = componentBox.x + (componentBox.width / 2);
                int topY = componentBox.y + 6;
                int bottomY = componentBox.y + componentBox.height - 6;

                if (expanded) {
                    g2d.drawLine(leftX, bottomY, centerX, topY);
                    g2d.drawLine(centerX, topY, rightX, bottomY);
                } else {
                    g2d.drawLine(leftX, topY, centerX, bottomY);
                    g2d.drawLine(centerX, bottomY, rightX, topY);
                }
            } finally {
                g2d.dispose();
            }
        }
    }
}
