package rroyo.JF.Decorations;

import java.awt.*;

/**
 * Immutable border style used to draw outlines around rectangular surfaces.
 *
 * @param color border color
 * @param thickness border thickness in pixels
 * @author rroyo
 */
public record Border(Color color, int thickness) {

    /**
     * Draws the border using square corners.
     *
     * @param g graphics context used for rendering
     * @param x top-left x-coordinate
     * @param y top-left y-coordinate
     * @param width border width in pixels
     * @param height border height in pixels
     */
    public void drawBorder(Graphics g, int x, int y, int width, int height) {
        drawBorder(g, x, y, width, height, 0);
    }

    /**
     * Draws the border using an optional rounded corner radius.
     *
     * @param g graphics context used for rendering
     * @param x top-left x-coordinate
     * @param y top-left y-coordinate
     * @param width border width in pixels
     * @param height border height in pixels
     * @param borderRadius corner radius in pixels
     */
    public void drawBorder(Graphics g, int x, int y, int width, int height, int borderRadius) {
        g.setColor(color);
        int clampedRadius = Math.max(0, borderRadius);

        for (int i = 0; i < thickness; i++) {
            int currentWidth = width - (i * 2);
            int currentHeight = height - (i * 2);
            if (currentWidth <= 0 || currentHeight <= 0) {
                return;
            }

            if (clampedRadius > 0) {
                int currentRadius = Math.max(0, clampedRadius - i);
                int arc = currentRadius * 2;
                g.drawRoundRect(x + i, y + i, currentWidth, currentHeight, arc, arc);
            } else {
                g.drawRect(x + i, y + i, currentWidth, currentHeight);
            }
        }
    }

}
