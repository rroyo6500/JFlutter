package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.Decorations.Border;
import rroyo.JF.Decorations.Decoration;
import rroyo.JF.JFComponents.JFComplexComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFCenter;
import rroyo.JF.JFComponents.SimpleComponents.JFContainer;
import rroyo.JF.JFComponents.SimpleComponents.JFText;
import rroyo.JF.JFEvents.JFActionEventSource;
import rroyo.JF.JFEvents.JFActionListener;
import rroyo.JF.JFEvents.JFHoverEventSource;
import rroyo.JF.JFEvents.JFHoverListener;

import java.awt.*;

public class JFButton extends JFComplexComponent implements JFActionEventSource, JFHoverEventSource {

    public JFButton(int width, int height, String text, Color color) {
        super(new JFContainer(width, height, new Decoration(color).setBorder(new Border(Color.BLACK, 2)))
                .addChild(new JFCenter(new JFText(text)))
        );
    }

    @Override
    public JFButton addActionListener(JFActionListener listener) {
        return (JFButton) JFActionEventSource.super.addActionListener(listener);
    }

    @Override
    public JFButton addHoverListener(JFHoverListener listener) {
        return (JFButton) JFHoverEventSource.super.addHoverListener(listener);
    }
}
