package rroyo.JF.Decorations;

import java.awt.*;

public class Decoration {

    private Color color;
    private Border border;

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

    public void draw(Graphics g, int x, int y, int width, int height) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
        if (border != null) {
            border.drawBorder(g, x, y, width, height);
        }
    }

}
