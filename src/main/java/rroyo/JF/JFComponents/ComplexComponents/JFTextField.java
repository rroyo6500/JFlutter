package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.Decorations.Border;
import rroyo.JF.Enums.Alignment;
import rroyo.JF.Enums.KeyEventTypes;
import rroyo.JF.JFComponents.BaseComponent.JFComplexComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFContainer;
import rroyo.JF.JFComponents.SimpleComponents.JFStack;
import rroyo.JF.JFComponents.SimpleComponents.JFText;
import rroyo.JF.JFEvents.JFKeyEvent;
import rroyo.JF.JFEvents.JFInteractiveComponent;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Basic single-line text input built from existing framework primitives.
 * <p>
 * The component wraps a {@link JFContainer} and overlays two {@link JFText} nodes:
 * one for the visible text and one for the cursor marker. It keeps its own mutable
 * text buffer, tracks the caret position, and reacts to keyboard events to update
 * the rendered viewport inside the fixed-size field.
 *
 * @author rroyo
 */
public class JFTextField extends JFComplexComponent implements JFInteractiveComponent {

    /**
     * Horizontal inset applied between the border and the rendered text.
     */
    private static final int HORIZONTAL_PADDING = 5;

    /**
     * Font used both for rendering and for width measurements.
     * <p>
     * Keeping a shared font instance ensures the viewport calculations match the
     * actual text that will be painted on screen.
     */
    private static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 12);

    /**
     * Visual text node that renders the visible portion of the current value.
     */
    private final JFText writedText;

    /**
     * Overlay text node used to show the caret as an underscore.
     */
    private final JFText cursorText;

    /**
     * Backing buffer containing the full text value, including parts that may be outside the viewport.
     */
    private final StringBuilder text = new StringBuilder();

    /**
     * Caret position inside {@link #text}, measured in characters.
     */
    private int cursorPosition = 0;

    /**
     * Creates a text field with a light-gray background and a black border.
     *
     * @param width field width in pixels
     * @param height field height in pixels
     */
    public JFTextField(int width, int height) {
        this(
                new JFContainer(width, height, Color.lightGray),
                new JFText(""),
                new JFText("_")
        );
    }

    /**
     * Builds the internal composition used by the text field.
     *
     * @param container background box acting as the field body
     * @param writedText text node that renders visible characters
     * @param cursorText text node that renders the caret overlay
     */
    private JFTextField(JFContainer container, JFText writedText, JFText cursorText) {
        super(() -> {
            Point textPosition = calculateTextPosition(container);

            writedText.setPosition(textPosition.x, textPosition.y);
            cursorText.setPosition(textPosition.x, textPosition.y);

            container.getDecoration().setBorder(new Border(Color.black, 1));

            container.addChild(
                    new JFStack(Alignment.CUSTOM).addChilds(
                            cursorText,
                            writedText
                    )
            );

            return container;
        });

        this.writedText = writedText;
        this.cursorText = cursorText;

        this.addKeyListener(e -> {
            handleKeyEvent(e);
        });

        refreshVisualState();
    }

    /**
     * Returns the full text currently stored in the field.
     *
     * @return current field value
     */
    public String getText() {
        return text.toString();
    }

    /**
     * Exposes the internal text node that paints the visible substring.
     *
     * @return visible-text component
     */
    public JFText getTextComponent() {
        return writedText;
    }

    /**
     * Replaces the current value and keeps the caret inside the new text bounds.
     *
     * @param value new text value, or {@code null} to clear the field
     * @return current text field for fluent updates
     */
    public JFTextField setText(String value) {
        text.setLength(0);
        text.append(value == null ? "" : value);
        cursorPosition = Math.min(cursorPosition, text.length());
        refreshVisualState();
        return this;
    }

    /**
     * Applies typing and navigation keys to the internal text buffer.
     *
     * @param e keyboard event routed by the active window
     */
    private void handleKeyEvent(JFKeyEvent e) {
        if (e.getType() == KeyEventTypes.KEY_TYPED) {
            char typedChar = e.getKeyChar();
            if (!Character.isISOControl(typedChar)) {
                text.insert(cursorPosition, typedChar);
                cursorPosition++;
                refreshVisualState();
            }
            return;
        }

        if (e.getType() != KeyEventTypes.KEY_PRESSED) {
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> cursorPosition = Math.max(0, cursorPosition - 1);
            case KeyEvent.VK_RIGHT -> cursorPosition = Math.min(text.length(), cursorPosition + 1);
            case KeyEvent.VK_HOME -> cursorPosition = 0;
            case KeyEvent.VK_END -> cursorPosition = text.length();
            case KeyEvent.VK_BACK_SPACE -> {
                if (cursorPosition > 0) {
                    text.deleteCharAt(cursorPosition - 1);
                    cursorPosition--;
                }
            }
            case KeyEvent.VK_DELETE -> {
                if (cursorPosition < text.length()) {
                    text.deleteCharAt(cursorPosition);
                }
            }
            default -> {
                return;
            }
        }

        refreshVisualState();
    }

    /**
     * Recomputes the visible substring, caret overlay and text position after a state change.
     */
    private void refreshVisualState() {
        JFContainer container = (JFContainer) getContent();
        Point textPosition = calculateTextPosition(container);
        VisibleTextState visibleTextState = calculateVisibleTextState(container);

        writedText.setPosition(textPosition.x, textPosition.y);
        cursorText.setPosition(textPosition.x, textPosition.y);
        writedText.setText(visibleTextState.visibleText());
        cursorText.setText(visibleTextState.cursorOverlay());
    }

    /**
     * Chooses the substring that fits in the available width while keeping the caret visible.
     * <p>
     * The viewport expands to the left from the caret first, then grows to the right until the
     * remaining horizontal space is exhausted. The returned overlay string mirrors the visible
     * prefix up to the caret and adds an underscore that acts as the cursor.
     *
     * @param container container that defines the available width
     * @return visible text and cursor overlay to render
     */
    private VisibleTextState calculateVisibleTextState(JFContainer container) {
        String fullText = text.toString();
        int availableWidth = Math.max(0, container.getWidth() - HORIZONTAL_PADDING);
        FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(TEXT_FONT);

        int viewportStart = Math.min(cursorPosition, fullText.length());
        while (viewportStart > 0 &&
                metrics.stringWidth(fullText.substring(viewportStart - 1, cursorPosition) + "_") <= availableWidth) {
            viewportStart--;
        }

        int viewportEnd = viewportStart;
        while (viewportEnd < fullText.length() &&
                metrics.stringWidth(fullText.substring(viewportStart, viewportEnd + 1)) <= availableWidth) {
            viewportEnd++;
        }

        String visibleText = fullText.substring(viewportStart, viewportEnd);
        int visibleCursorPosition = Math.max(0, cursorPosition - viewportStart);
        String cursorOverlay = visibleText.substring(0, Math.min(visibleCursorPosition, visibleText.length())) + "_";

        return new VisibleTextState(visibleText, cursorOverlay);
    }

    /**
     * Computes the top-left position used to paint text inside the field body.
     *
     * @param container field container whose size defines the vertical centering
     * @return local drawing position for both text layers
     */
    private static Point calculateTextPosition(JFContainer container) {
        FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(TEXT_FONT);
        int x = HORIZONTAL_PADDING;
        int y = Math.max(0, (container.getHeight() - metrics.getHeight()) / 2);
        return new Point(x, y);
    }

    /**
     * Immutable snapshot of the text actually rendered after viewport clipping.
     *
     * @param visibleText substring that fits inside the field
     * @param cursorOverlay substring plus caret marker used by the overlay node
     */
    private record VisibleTextState(String visibleText, String cursorOverlay) {
    }
}
