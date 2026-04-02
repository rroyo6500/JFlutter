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

public class JFTextField extends JFComplexComponent implements JFInteractiveComponent {

    private static final int HORIZONTAL_PADDING = 5;
    private static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 12);

    private final JFText writedText;
    private final JFText cursorText;
    private final StringBuilder text = new StringBuilder();
    private int cursorPosition = 0;

    public JFTextField(int width, int height) {
        this(
                new JFContainer(width, height, Color.lightGray),
                new JFText(""),
                new JFText("_")
        );
    }

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

    public String getText() {
        return text.toString();
    }

    public JFText getTextComponent() {
        return writedText;
    }

    public JFTextField setText(String value) {
        text.setLength(0);
        text.append(value == null ? "" : value);
        cursorPosition = Math.min(cursorPosition, text.length());
        refreshVisualState();
        return this;
    }

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

    private void refreshVisualState() {
        JFContainer container = (JFContainer) getContent();
        Point textPosition = calculateTextPosition(container);
        VisibleTextState visibleTextState = calculateVisibleTextState(container);

        writedText.setPosition(textPosition.x, textPosition.y);
        cursorText.setPosition(textPosition.x, textPosition.y);
        writedText.setText(visibleTextState.visibleText());
        cursorText.setText(visibleTextState.cursorOverlay());
    }

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

    private static Point calculateTextPosition(JFContainer container) {
        FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(TEXT_FONT);
        int x = HORIZONTAL_PADDING;
        int y = Math.max(0, (container.getHeight() - metrics.getHeight()) / 2);
        return new Point(x, y);
    }

    private record VisibleTextState(String visibleText, String cursorOverlay) {
    }
}
