package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.Decorations.Border;
import rroyo.JF.JFComponents.BaseComponent.JFComplexComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFCenter;
import rroyo.JF.JFComponents.SimpleComponents.JFContainer;
import rroyo.JF.JFComponents.SimpleComponents.JFText;
import rroyo.JF.JFEvents.JFActionListener;
import rroyo.JF.JFEvents.JFHoverListener;
import rroyo.JF.JFEvents.JFInteractiveComponent;
import rroyo.JF.JFEvents.JFKeyListener;

import java.awt.*;

/**
 * Reusable button widget built from a container and a centered text label.
 * <p>
 * The class focuses on composition rather than custom drawing: internally it builds a fixed-size
 * {@link JFContainer} and places the provided {@link JFText} at its center. By implementing
 * {@link JFInteractiveComponent}, the button can directly register action, hover and keyboard
 * listeners while still exposing a simple widget-style API.
 *
 * @author rroyo
 */
public class JFButton extends JFComplexComponent implements JFInteractiveComponent {

    /**
     * Text component displayed inside the button.
     */
    private final JFText text;

    public JFButton(int width, int height, String text) {
        this(width, height, new JFText(text));
    }

    /**
     * Creates a button with fixed size and centered text content.
     *
     * @param width button width in pixels
     * @param height button height in pixels
     * @param text text component shown at the center of the button
     */
    public JFButton(int width, int height, JFText text) {
        super(() -> {
            if (text == null) throw new NullPointerException("Text cannot be null");

            JFContainer container = new JFContainer(width, height, Color.lightGray);
            container.getDecoration().setBorder(new Border(Color.black, 1));

            return container.addChild(new JFCenter(text));
        });
        this.text = text;
    }

    /**
     * Returns the text component owned by the button so callers can further style it.
     *
     * @return button label component
     */
    public JFText getText() {
        return text;
    }

    /**
     * Narrows the fluent return type for action listeners.
     *
     * @param listener action listener to register
     * @return current button
     */
    @Override
    public JFButton addActionListener(JFActionListener listener) {
        return (JFButton) JFInteractiveComponent.super.addActionListener(listener);
    }

    /**
     * Narrows the fluent return type for hover listeners.
     *
     * @param listener hover listener to register
     * @return current button
     */
    @Override
    public JFButton addHoverListener(JFHoverListener listener) {
        return (JFButton) JFInteractiveComponent.super.addHoverListener(listener);
    }

    /**
     * Narrows the fluent return type for keyboard listeners.
     *
     * @param listener key listener to register
     * @return current button
     */
    @Override
    public JFButton addKeyListener(JFKeyListener listener) {
        return (JFButton) JFInteractiveComponent.super.addKeyListener(listener);
    }

}
