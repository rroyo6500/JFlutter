package rroyo.JF.JFComponents.SimpleComponents;

import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class JFText extends JFComponent {

    private final String text;
    private Font font = new Font("Arial", Font.PLAIN, 12);
    private Color color = Color.BLACK;

    public JFText(String text) {
        this.text = text;
    }

    public JFText setFont(Font font) {
        this.font = font;
        return this;
    }

    public JFText setColor(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public void layoutRecalculate() {
        FontRenderContext frc = new FontRenderContext(null, true, true);

        Rectangle2D bounds = font.getStringBounds(text, frc);

        int width = (int) Math.ceil(bounds.getWidth());
        int height = (int) Math.ceil(bounds.getHeight());

        setSize(width, height);
    }

    @Override
    public void design(Graphics g) {
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, componentBox.x, componentBox.y + componentBox.height);
    }
}
