package rroyo.JF.Decorations;

import java.awt.*;

/**
 * Visual decoration model for component backgrounds, borders, and shadows.
 *
 * @author rroyo
 */
public class Decoration {

    /**
     * Fill color used for the component background.
     */
    private Color color;
    /**
     * Optional border style rendered over the background.
     */
    private Border border;
    /**
     * Optional shadow style rendered before the background.
     */
    private BoxShadow shadow;
    /**
     * Corner radius applied to the background and border.
     */
    private int borderRadius;

    /**
     * Creates a decoration configured with a background color.
     *
     * @param color background color to apply
     */
    public Decoration(Color color) {
        this.color = color;
    }

    /**
     * Returns the configured background color.
     *
     * @return current background color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Updates the background color.
     *
     * @param color new background color
     * @return current decoration instance
     */
    public Decoration setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Returns the configured border style.
     *
     * @return current border, or {@code null} when absent
     */
    public Border getBorder() {
        return border;
    }

    /**
     * Sets the border style for this decoration.
     *
     * @param border border to render, or {@code null} to disable it
     * @return current decoration instance
     */
    public Decoration setBorder(Border border) {
        this.border = border;
        return this;
    }

    /**
     * Returns the configured shadow style.
     *
     * @return current shadow, or {@code null} when absent
     */
    public BoxShadow getShadow() {
        return shadow;
    }

    /**
     * Sets the shadow style for this decoration.
     *
     * @param shadow shadow to render, or {@code null} to disable it
     * @return current decoration instance
     */
    public Decoration setShadow(BoxShadow shadow) {
        this.shadow = shadow;
        return this;
    }

    /**
     * Returns the configured corner radius.
     *
     * @return corner radius in pixels
     */
    public int getBorderRadius() {
        return borderRadius;
    }

    /**
     * Sets the corner radius used for rounded rendering.
     *
     * @param borderRadius requested corner radius in pixels
     * @return current decoration instance
     */
    public Decoration setBorderRadius(int borderRadius) {
        this.borderRadius = Math.max(0, borderRadius);
        return this;
    }

    /**
     * Alias for {@link #setBorderRadius(int)}.
     *
     * @param borderRadius requested corner radius in pixels
     * @return current decoration instance
     */
    public Decoration setRadius(int borderRadius) {
        return setBorderRadius(borderRadius);
    }

    /**
     * Renders the complete decoration at the specified bounds.
     *
     * @param g graphics context used for rendering
     * @param x top-left x-coordinate
     * @param y top-left y-coordinate
     * @param width decoration width
     * @param height decoration height
     */
    public void draw(Graphics g, int x, int y, int width, int height) {
        int clampedRadius = Math.clamp(Math.min(width, height) / 2, 0, borderRadius);
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
