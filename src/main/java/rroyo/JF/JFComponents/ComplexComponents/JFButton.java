package rroyo.JF.JFComponents.ComplexComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.Decorations.Border;
import rroyo.JF.Decorations.Decoration;
import rroyo.JF.JFComponents.JFComplexComponent;
import rroyo.JF.JFComponents.JFComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFCenter;
import rroyo.JF.JFComponents.SimpleComponents.JFContainer;
import rroyo.JF.JFComponents.SimpleComponents.JFText;
import rroyo.JF.JFEvents.JFActionListener;
import rroyo.JF.JFEvents.JFInteractiveEventSource;
import rroyo.JF.JFEvents.JFHoverListener;

import java.awt.*;

/**
 * Clickable button component composed of a decorated container and centered text.
 *
 * @author rroyo
 */
public class JFButton extends JFComplexComponent implements JFInteractiveEventSource {

    /**
     * Text child displayed inside the button.
     */
    private final JFText text;

    /**
     * Creates a button with fixed size, text content, and background color.
     *
     * @param width button width in pixels
     * @param height button height in pixels
     * @param text text component shown inside the button
     * @param color background color for the button container
     */
    public JFButton(int width, int height, JFText text, Color color) {
        super(() ->
                new JFContainer(width, height,
                        new Decoration(color)
                                .setBorder(new Border(Color.black, 2))
                                .setBorderRadius(10)
                ).addChild(new JFCenter(text))
        );
        this.text = text;
    }

    /**
     * Returns the text component used by this button.
     *
     * @return button text component
     */
    public JFText getText() {
        return text;
    }

    @Override
    public JFButton addActionListener(JFActionListener listener) {
        return (JFButton) JFInteractiveEventSource.super.addActionListener(listener);
    }

    @Override
    public JFButton addHoverListener(JFHoverListener listener) {
        return (JFButton) JFInteractiveEventSource.super.addHoverListener(listener);
    }

}
