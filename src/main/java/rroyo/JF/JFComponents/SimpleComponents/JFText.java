package rroyo.JF.JFComponents.SimpleComponents;

import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class JFText extends JFComponent {

    private String text;
    private Font font = new Font("Arial", Font.PLAIN, 12);
    private Color color = Color.BLACK;
    private Color backgroundColor;

    public JFText(String text) {
        this.text = text;
    }

    public JFText setText(String text) {
        this.text = text;
        return this;
    }
    public JFText setFont(Font font) {
        this.font = font;
        return this;
    }
    public JFText setColor(Color color) {
        this.color = color;
        return this;
    }
    public JFText setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    @Override
    protected void layoutRecalculate() {
        FontRenderContext frc = new FontRenderContext(null, true, true);

        Rectangle2D bounds = font.getStringBounds(text, frc);

        int width = (int) Math.ceil(bounds.getWidth());
        int height = (int) Math.ceil(bounds.getHeight());

        setSize(width, height);
    }

    @Override
    protected void design(Graphics g) {
        if (backgroundColor != null) {
            g.setColor(backgroundColor);
            g.fillRect(componentBox.x, componentBox.y, componentBox.width, componentBox.height);
        }

        g.setColor(color);
        g.setFont(font);
        g.drawString(text, componentBox.x, componentBox.y + componentBox.height);
    }
}
