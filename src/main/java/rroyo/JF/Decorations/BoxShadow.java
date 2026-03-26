package rroyo.JF.Decorations;

import java.awt.*;

public record BoxShadow(Color shadowColor, int offsetX, int offsetY) {

    public void drawShadow(Graphics g, int x, int y, int width, int height) {
        g.setColor(shadowColor);
        g.fillRect(x + offsetX, y + offsetY, width, height);
    }

}
