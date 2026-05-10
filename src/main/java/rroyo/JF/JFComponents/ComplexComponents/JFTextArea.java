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
 * Multi-line text input component built from existing framework primitives.
 * <p>
 * Similar to {@link JFTextField}, but supports multiple lines of text with line breaks.
 * The component wraps a {@link JFContainer} and overlays two {@link JFText} nodes:
 * one for the visible text and one for the cursor marker. It keeps its own mutable
 * text buffer, tracks the caret position, and reacts to keyboard events to update
 * the rendered viewport inside the fixed-size field.
 * <p>
 * Unlike {@link JFTextField}, the text is positioned in the top-left corner with
 * a 5-pixel margin from the edges.
 *
 * @author rroyo
 */
public class JFTextArea extends JFComplexComponent implements JFInteractiveComponent {

    /**
     * Inset applied between the border and the rendered text.
     */
    private static final int PADDING = 5;

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
    private final JFText displayedText;

    /**
     * Overlay text node used to show the caret as an underscore.
     */
    private final JFText cursorText;

    /**
     * Backing buffer containing the full text value, including parts that may be outside the viewport.
     */
    private final StringBuilder text = new StringBuilder();

    /**
     * Caret position inside {@link #text}, measured in characters (not lines).
     */
    private int cursorPosition = 0;

    /**
     * Creates a text area with a light-gray background and a black border.
     *
     * @param width field width in pixels
     * @param height field height in pixels
     */
    public JFTextArea(int width, int height) {
        this(
                new JFContainer(width, height, Color.lightGray),
                new JFText(""),
                new JFText("_")
        );
    }

    /**
     * Builds the internal composition used by the text area.
     *
     * @param container background box acting as the field body
     * @param displayedText text node that renders visible characters
     * @param cursorText text node that renders the caret overlay
     */
    private JFTextArea(JFContainer container, JFText displayedText, JFText cursorText) {
        super(() -> {
            Point textPosition = new Point(PADDING, PADDING);

            displayedText.setPosition(textPosition.x, textPosition.y);
            cursorText.setPosition(textPosition.x, textPosition.y);

            container.getDecoration().setBorder(new Border(Color.black, 1));

            container.addChild(
                    new JFStack(Alignment.CUSTOM).addChilds(
                            cursorText,
                            displayedText
                    )
            );

            return container;
        });

        this.displayedText = displayedText;
        this.cursorText = cursorText;

        this.addKeyListener(e -> {
            handleKeyEvent(e);
        });

        refreshVisualState();
    }

    /**
     * Returns the full text currently stored in the area.
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
        return displayedText;
    }

    /**
     * Replaces the current value and keeps the caret inside the new text bounds.
     *
     * @param value new text value, or {@code null} to clear the field
     * @return current text area for fluent updates
     */
    public JFTextArea setText(String value) {
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
            if (!Character.isISOControl(typedChar) || typedChar == '\n') {
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
            case KeyEvent.VK_UP -> cursorPosition = moveUp();
            case KeyEvent.VK_DOWN -> cursorPosition = moveDown();
            case KeyEvent.VK_HOME -> cursorPosition = moveToLineStart();
            case KeyEvent.VK_END -> cursorPosition = moveToLineEnd();
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
            case KeyEvent.VK_ENTER -> {
                text.insert(cursorPosition, '\n');
                cursorPosition++;
            }
            default -> {
                return;
            }
        }

        refreshVisualState();
    }

    /**
     * Moves the cursor up one line, preserving column position when possible.
     *
     * @return new cursor position
     */
    private int moveUp() {
        int currentLineStart = findLineStart(cursorPosition);
        if (currentLineStart == 0) {
            return cursorPosition;
        }

        int columnInLine = cursorPosition - currentLineStart;
        int previousLineStart = findLineStart(currentLineStart - 1);
        int previousLineEnd = currentLineStart - 1;

        int previousLineLength = previousLineEnd - previousLineStart;
        int newColumn = Math.min(columnInLine, previousLineLength);

        return previousLineStart + newColumn;
    }

    /**
     * Moves the cursor down one line, preserving column position when possible.
     *
     * @return new cursor position
     */
    private int moveDown() {
        int currentLineStart = findLineStart(cursorPosition);
        int currentLineEnd = findLineEnd(cursorPosition);

        if (currentLineEnd >= text.length()) {
            return cursorPosition;
        }

        int columnInLine = cursorPosition - currentLineStart;
        int nextLineStart = currentLineEnd + 1;
        int nextLineEnd = findLineEnd(nextLineStart);

        int nextLineLength = nextLineEnd - nextLineStart;
        int newColumn = Math.min(columnInLine, nextLineLength);

        return nextLineStart + newColumn;
    }

    /**
     * Moves the cursor to the start of the current line.
     *
     * @return position of the line start
     */
    private int moveToLineStart() {
        return findLineStart(cursorPosition);
    }

    /**
     * Moves the cursor to the end of the current line.
     *
     * @return position of the line end
     */
    private int moveToLineEnd() {
        return findLineEnd(cursorPosition);
    }

    /**
     * Finds the position of the start of the line containing the given position.
     *
     * @param position position within the text
     * @return position of the line start (0 if at beginning of text)
     */
    private int findLineStart(int position) {
        position = Math.max(0, Math.min(position, text.length()));
        int lineStart = position;
        while (lineStart > 0 && text.charAt(lineStart - 1) != '\n') {
            lineStart--;
        }
        return lineStart;
    }

    /**
     * Finds the position of the end of the line containing the given position.
     *
     * @param position position within the text
     * @return position of the line end (length of text if at end)
     */
    private int findLineEnd(int position) {
        position = Math.max(0, Math.min(position, text.length()));
        int lineEnd = position;
        while (lineEnd < text.length() && text.charAt(lineEnd) != '\n') {
            lineEnd++;
        }
        return lineEnd;
    }

    /**
     * Recomputes the visible text and caret overlay after a state change.
     */
    private void refreshVisualState() {
        JFContainer container = (JFContainer) getContent();
        VisibleTextState visibleTextState = calculateVisibleTextState(container);

        displayedText.setText(visibleTextState.visibleText());
        cursorText.setText(visibleTextState.cursorOverlay());
    }

    /**
     * Chooses the substring that fits in the available space while keeping the caret visible.
     * <p>
     * For a multi-line text area, this calculates which lines should be visible based on
     * the available height and width, ensuring the cursor position is always visible.
     *
     * @param container container that defines the available width and height
     * @return visible text and cursor overlay to render
     */
    private VisibleTextState calculateVisibleTextState(JFContainer container) {
        String fullText = text.toString();
        int availableWidth = Math.max(0, container.getWidth() - 2 * PADDING);
        int availableHeight = Math.max(0, container.getHeight() - 2 * PADDING);
        FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(TEXT_FONT);
        int lineHeight = metrics.getHeight();

        // Calculate which lines are visible
        String[] lines = fullText.split("\n", -1);
        int[] linePositions = new int[lines.length];
        int currentPos = 0;
        for (int i = 0; i < lines.length; i++) {
            linePositions[i] = currentPos;
            currentPos += lines[i].length() + 1; // +1 for newline
        }

        // Find which line the cursor is on
        int cursorLine = 0;
        for (int i = 0; i < linePositions.length; i++) {
            if (cursorPosition >= linePositions[i]) {
                cursorLine = i;
            } else {
                break;
            }
        }

        // Calculate visible line range
        int maxVisibleLines = Math.max(1, availableHeight / lineHeight);
        int firstVisibleLine = Math.max(0, cursorLine - maxVisibleLines + 1);
        int lastVisibleLine = Math.min(lines.length - 1, firstVisibleLine + maxVisibleLines - 1);

        // Build visible text
        StringBuilder visibleTextBuilder = new StringBuilder();
        for (int i = firstVisibleLine; i <= lastVisibleLine; i++) {
            if (i > firstVisibleLine) {
                visibleTextBuilder.append("\n");
            }
            visibleTextBuilder.append(lines[i]);
        }

        String visibleText = visibleTextBuilder.toString();

        // Calculate cursor position within visible text
        int visibleCursorPosition = cursorPosition;
        for (int i = 0; i < firstVisibleLine; i++) {
            visibleCursorPosition -= lines[i].length() + 1;
        }

        // Build cursor overlay
        String cursorOverlay = visibleText.substring(0, Math.min(Math.max(0, visibleCursorPosition), visibleText.length())) + "_";

        return new VisibleTextState(visibleText, cursorOverlay);
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

