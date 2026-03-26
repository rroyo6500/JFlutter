package rroyo.JF.Decorations;

import java.awt.*;

public class Decoration {

    private Color color;
    private Border border;
    private BoxShadow shadow;
    private int borderRadius;

    public Decoration(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Decoration setColor(Color color) {
        this.color = color;
        return this;
    }

    public Border getBorder() {
        return border;
    }

    public Decoration setBorder(Border border) {
        this.border = border;
        return this;
    }

    public BoxShadow getShadow() {
        return shadow;
    }

    public Decoration setShadow(BoxShadow shadow) {
        this.shadow = shadow;
        return this;
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    public Decoration setBorderRadius(int borderRadius) {
        this.borderRadius = Math.max(0, borderRadius);
        return this;
    }

    public Decoration setRadius(int borderRadius) {
        return setBorderRadius(borderRadius);
    }

    public void draw(Graphics g, int x, int y, int width, int height) {
        int clampedRadius = Math.min(borderRadius, Math.max(0, Math.min(width, height) / 2));
        int arc = clampedRadius * 2;

        if (shadow != null)
            shadow.drawShadow(g, x, y, width, height, clampedRadius);

        g.setColor(color);
        if (arc > 0) {
            g.fillRoundRect(x, y, width, height, arc, arc);
        } else {
            g.fillRect(x, y, width, height);
        }

        if (border != null)
            border.drawBorder(g, x, y, width, height, clampedRadius);
    }

}
