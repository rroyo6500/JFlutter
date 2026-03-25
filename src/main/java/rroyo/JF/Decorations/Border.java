package rroyo.JF.Decorations;

import java.awt.*;

public class Border {

    private final Color color;
    private final int thickness;

    public Border(Color color, int thickness) {
        this.color = color;
        this.thickness = thickness;
    }

    public void drawBorder(Graphics g, int x, int y, int width, int height) {
        g.setColor(color);
        for (int i = 0; i < thickness; i++) {
            g.drawRect(x + i, y + i, width - (i * 2), height - (i * 2));
        }
    }

}
