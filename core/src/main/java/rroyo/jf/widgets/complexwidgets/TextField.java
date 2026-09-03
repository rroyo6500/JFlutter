package rroyo.jf.widgets.complexwidgets;

import lombok.Getter;
import rroyo.jf.enums.Alignment;
import rroyo.jf.enums.KeyEventTypes;
import rroyo.jf.events.Key.KeyEvent;
import rroyo.jf.events.KeyWidget;
import rroyo.jf.widgets.basewidgets.ComplexWidget;
import rroyo.jf.widgets.basewidgets.Widget;
import rroyo.jf.widgets.simplewidgets.Text;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

public class TextField extends ComplexWidget implements KeyWidget<TextField> {

    private static final FontRenderContext TEXT_FRC =
            new FontRenderContext(
                    null,
                    true,
                    true
            );

    @Getter
    private static final int HORIZONTAL_PADDING = 5;

    @Getter
    private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 14);

    @Getter
    private final Text writedText = (Text) new Text("")
            .setFont(DEFAULT_FONT)
            .setColor(Color.BLACK)
            .setOverflow(true);

    @Getter
    private final Text cursorText = (Text) new Text("_")
            .setFont(DEFAULT_FONT)
            .setColor(Color.BLACK)
            .setOverflow(true);
    
    private final SizedStack stack;

    private final StringBuilder text = new StringBuilder();
    
    private int cursorPosition = 0;

    private int scrollOffset = 0;

    public TextField(int width, int height) {
        stack = (SizedStack) new SizedStack(Alignment.CUSTOM, new Dimension(width, height),
                cursorText,
                writedText
        ).setClipChildrenToBounds(true);
    }

    @Override
    protected Widget build() {
        addKeyListener(this::handleKeyEvent);
        refreshVisualState();
        return stack;
    }

    private void refreshVisualState() {

        writedText.setText(text.toString());
        cursorText.setText("_");

        updateHorizontalScroll();

        int textY = calculateTextY();

        int textX =
                HORIZONTAL_PADDING
                        - scrollOffset;

        int textBeforeCursorWidth =
                measureTextWidth(
                        text.substring(
                                0,
                                cursorPosition
                        )
                );

        int cursorX =
                HORIZONTAL_PADDING
                        - scrollOffset
                        + textBeforeCursorWidth;

        writedText.setPosition(
                textX,
                textY
        );

        cursorText.setPosition(
                cursorX,
                textY
        );
    }

    private void updateHorizontalScroll() {

        int availableWidth =
                getAvailableTextWidth();

        int totalTextWidth =
                measureTextWidth(
                        text.toString()
                );

        int cursorX =
                measureTextWidth(
                        text.substring(
                                0,
                                cursorPosition
                        )
                );

        int cursorWidth =
                measureTextWidth("_");

        int maxScroll =
                Math.max(
                        0,
                        totalTextWidth - availableWidth
                );

        if (cursorX < scrollOffset) {
            scrollOffset = cursorX;
        }

        if (cursorX + cursorWidth
                > scrollOffset + availableWidth) {

            scrollOffset =
                    cursorX
                            + cursorWidth
                            - availableWidth;
        }

        scrollOffset =
                Math.clamp(
                        scrollOffset,
                        0,
                        maxScroll
                );
    }

    private int getAvailableTextWidth() {
        return Math.max(
                0,
                stack.getSize().width - (HORIZONTAL_PADDING * 2)
        );
    }

    private int calculateTextY() {

        LineMetrics metrics =
                getFont().getLineMetrics(
                        "",
                        TEXT_FRC
                );

        int lineHeight =
                (int) Math.ceil(
                        metrics.getHeight()
                );

        return Math.max(
                0,
                (stack.getSize().height - lineHeight) / 2
        );
    }

    private int measureTextWidth(String value) {

        if (value.isEmpty()) {
            return 0;
        }

        Rectangle2D bounds =
                getFont().getStringBounds(
                        value,
                        TEXT_FRC
                );

        return (int) Math.ceil(
                bounds.getWidth()
        );
    }

    private void handleKeyEvent(KeyEvent e) {

        if (e.getType() == KeyEventTypes.KEY_TYPED) {

            char typedChar = e.getKeyChar();

            if (!Character.isISOControl(typedChar)) {

                text.insert(
                        cursorPosition,
                        typedChar
                );

                cursorPosition++;

                refreshVisualState();
            }

            return;
        }

        if (e.getType() != KeyEventTypes.KEY_PRESSED) {
            return;
        }

        switch (e.getKeyCode()) {

            case java.awt.event.KeyEvent.VK_LEFT ->
                    cursorPosition = Math.max(
                            0,
                            cursorPosition - 1
                    );

            case java.awt.event.KeyEvent.VK_RIGHT ->
                    cursorPosition = Math.min(
                            text.length(),
                            cursorPosition + 1
                    );

            case java.awt.event.KeyEvent.VK_HOME ->
                    cursorPosition = 0;

            case java.awt.event.KeyEvent.VK_END ->
                    cursorPosition = text.length();

            case java.awt.event.KeyEvent.VK_BACK_SPACE -> {
                if (cursorPosition > 0) {

                    text.deleteCharAt(
                            cursorPosition - 1
                    );

                    cursorPosition--;
                }
            }

            case java.awt.event.KeyEvent.VK_DELETE -> {
                if (cursorPosition < text.length()) {

                    text.deleteCharAt(
                            cursorPosition
                    );
                }
            }

            default -> {
                return;
            }
        }

        refreshVisualState();
    }

    public TextField setText(String value) {
        text.setLength(0);
        text.append(value == null ? "" : value);

        cursorPosition = Math.min(
                cursorPosition,
                text.length()
        );

        refreshVisualState();

        return this;
    }

    public TextField setFont(Font font) {
        if (font == null) {
            throw new IllegalArgumentException(
                    "Font cannot be null."
            );
        }

        writedText.setFont(font);
        cursorText.setFont(font);

        refreshVisualState();

        return this;
    }

    public TextField setTextColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException(
                    "Text color cannot be null."
            );
        }

        writedText.setColor(color);
        cursorText.setColor(color);

        return this;
    }

    public String getText() {
        return text.toString();
    }

    public Font getFont() {
        return writedText.getFont();
    }
    
}
