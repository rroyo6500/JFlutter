package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.Decorations.Border;
import rroyo.JF.Enums.ActionEventTypes;
import rroyo.JF.Enums.HoverEventTypes;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;
import rroyo.JF.JFEvents.JFInteractiveComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

/**
 * Reusable vertical scrollbar for scrollable widgets.
 * <p>
 * The scrollbar models a viewport size, a content size and a current scroll value.
 * It supports clicking on the track, dragging the thumb and listening for value changes.
 *
 * @author rroyo
 */
public class JFScrollBar extends JFComponent implements JFInteractiveComponent {

    private static final int MIN_THUMB_SIZE = 26;
    private static final Color TRACK_COLOR = new Color(241, 245, 249);
    private static final Color TRACK_BORDER_COLOR = new Color(203, 213, 225);
    private static final Color THUMB_COLOR = new Color(148, 163, 184);
    private static final Color THUMB_HOVER_COLOR = new Color(100, 116, 139);
    private static final Color THUMB_DRAG_COLOR = new Color(71, 85, 105);

    private final List<IntConsumer> valueChangeListeners = new ArrayList<>();

    private int viewportSize = 1;
    private int contentSize = 1;
    private int value;
    private boolean hovered;
    private boolean dragging;
    private int dragOffsetY;

    /**
     * Creates a vertical scrollbar.
     *
     * @param width scrollbar width
     * @param height scrollbar height
     */
    public JFScrollBar(int width, int height) {
        setSize(width, height);

        addActionListener(this::handleActionEvent);
        addHoverListener(event -> {
            hovered = event.getType() != HoverEventTypes.EXIT;

            if (dragging && event.getType() == HoverEventTypes.MOVE) {
                updateValueFromMouse(event.getMouseY());
            }
        });
    }

    /**
     * Updates the viewport and content sizes used to compute the thumb.
     *
     * @param viewportSize visible size in pixels
     * @param contentSize total content size in pixels
     * @return current scrollbar
     */
    public JFScrollBar setRange(int viewportSize, int contentSize) {
        this.viewportSize = Math.max(1, viewportSize);
        this.contentSize = Math.max(1, contentSize);
        setValue(value);
        return this;
    }

    /**
     * Returns the current scroll value.
     *
     * @return current scroll offset in pixels
     */
    public int getValue() {
        return value;
    }

    /**
     * Updates the current scroll value.
     *
     * @param value new scroll offset in pixels
     * @return current scrollbar
     */
    public JFScrollBar setValue(int value) {
        int clampedValue = Math.max(0, Math.min(getMaxValue(), value));
        if (this.value == clampedValue) {
            return this;
        }

        this.value = clampedValue;
        notifyValueChangeListeners();
        return this;
    }

    /**
     * Returns whether scrolling is actually needed.
     *
     * @return {@code true} when content is larger than the viewport
     */
    public boolean isScrollable() {
        return contentSize > viewportSize;
    }

    /**
     * Registers a listener invoked whenever the scroll value changes.
     *
     * @param listener listener to add
     * @return current scrollbar
     */
    public JFScrollBar addValueChangeListener(IntConsumer listener) {
        if (listener == null) throw new IllegalArgumentException("Listener cannot be null");
        valueChangeListeners.add(listener);
        return this;
    }

    /**
     * Removes a previously registered value-change listener.
     *
     * @param listener listener to remove
     * @return current scrollbar
     */
    public JFScrollBar removeValueChangeListener(IntConsumer listener) {
        valueChangeListeners.remove(listener);
        return this;
    }

    @Override
    protected void layoutRecalculate() {
    }

    @Override
    protected void design(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(TRACK_COLOR);
            g2d.fillRoundRect(componentBox.x, componentBox.y, componentBox.width, componentBox.height, componentBox.width, componentBox.width);
            new Border(TRACK_BORDER_COLOR, 1).drawBorder(g2d, componentBox.x, componentBox.y, componentBox.width, componentBox.height, componentBox.width / 2);

            Rectangle thumbBounds = getThumbBounds();
            Color thumbColor = dragging ? THUMB_DRAG_COLOR : hovered ? THUMB_HOVER_COLOR : THUMB_COLOR;
            g2d.setColor(thumbColor);
            g2d.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, thumbBounds.width, thumbBounds.width);
        } finally {
            g2d.dispose();
        }
    }

    private void handleActionEvent(rroyo.JF.JFEvents.JFActionEvent event) {
        if (!isScrollable()) {
            return;
        }

        if (event.getAction() == ActionEventTypes.DOWN) {
            Rectangle thumbBounds = getThumbBounds();
            if (thumbBounds.contains(event.getMouseX(), event.getMouseY())) {
                dragging = true;
                dragOffsetY = event.getMouseY() - thumbBounds.y;
            } else {
                dragging = true;
                dragOffsetY = thumbBounds.height / 2;
                updateValueFromMouse(event.getMouseY());
            }
            return;
        }

        if (event.getAction() == ActionEventTypes.UP) {
            dragging = false;
        }
    }

    private void updateValueFromMouse(int mouseY) {
        Rectangle thumbBounds = getThumbBounds();
        int trackTravel = Math.max(1, componentBox.height - thumbBounds.height);
        int thumbTop = Math.max(componentBox.y, Math.min(componentBox.y + trackTravel, mouseY - dragOffsetY));
        float ratio = (thumbTop - componentBox.y) / (float) trackTravel;
        setValue(Math.round(ratio * getMaxValue()));
    }

    private int getMaxValue() {
        return Math.max(0, contentSize - viewportSize);
    }

    private Rectangle getThumbBounds() {
        int thumbHeight = isScrollable()
                ? Math.max(MIN_THUMB_SIZE, Math.round((viewportSize / (float) contentSize) * componentBox.height))
                : componentBox.height;
        thumbHeight = Math.min(componentBox.height, thumbHeight);

        int trackTravel = Math.max(0, componentBox.height - thumbHeight);
        int thumbY = componentBox.y;
        if (trackTravel > 0 && getMaxValue() > 0) {
            float ratio = value / (float) getMaxValue();
            thumbY += Math.round(ratio * trackTravel);
        }

        return new Rectangle(componentBox.x, thumbY, componentBox.width, thumbHeight);
    }

    private void notifyValueChangeListeners() {
        for (IntConsumer listener : List.copyOf(valueChangeListeners)) {
            listener.accept(value);
        }
    }
}
