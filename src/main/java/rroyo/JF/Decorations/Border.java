package rroyo.JF.Decorations;

import java.awt.*;

public record Border(Color color, int thickness) {

    public void drawBorder(Graphics g, int x, int y, int width, int height) {
        g.setColor(color);
        for (int i = 0; i < thickness; i++) {
            g.drawRect(x + i, y + i, width - (i * 2), height - (i * 2));
        }
    }

}
