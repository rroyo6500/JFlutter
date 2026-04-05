package rroyo.JF.JFComponents.SimpleComponents;

import rroyo.JF.JFComponents.BaseComponent.JFComponent;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

/**
 * Text-rendering component with configurable content, font and colors.
 * <p>
 * {@code JFText} is a leaf component that measures its own size from the active font metrics
 * and then draws the text clipped to its bounding box. It can also paint an optional background
 * behind the text, which is useful when composing labels or badges.
 *
 * @author rroyo
 */
public class JFText extends JFComponent {

    /**
     * Text string currently displayed by the component.
     */
    private String text;

    /**
     * Font used for text measurement and rendering.
     */
    private Font font = new Font("Arial", Font.PLAIN, 12);

    /**
     * Foreground color used to draw the text glyphs.
     */
    private Color color = Color.BLACK;

    /**
     * Optional background color drawn behind the text.
     */
    private Color backgroundColor;

    /**
     * Creates a text component with the supplied initial string.
     *
     * @param text initial text content
     */
    public JFText(String text) {
        this.text = text;
    }

    /**
     * Replaces the displayed text and invalidates layout so the component can be remeasured.
     *
     * @param text new text content
     * @return current text component
     */
    public JFText setText(String text) {
        this.text = text;
        invalidateLayout();
        return this;
    }

    public String getText() {
        return text;
    }

    /**
     * Changes the font used for both measurement and rendering.
     *
     * @param font new font configuration
     * @return current text component
     */
    public JFText setFont(Font font) {
        this.font = font;
        invalidateLayout();
        return this;
    }

    /**
     * Changes the text color.
     *
     * @param color new foreground color
     * @return current text component
     */
    public JFText setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Sets the background color drawn underneath the text.
     *
     * @param backgroundColor new background color
     * @return current text component
     */
    public JFText setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * Measures the text using the current font and computes the component bounds.
     * <p>
     * The natural text width is calculated from both the font render context and toolkit metrics,
     * using the maximum of both values for safety. If the parent has a constrained width, the text
     * box is clipped so it does not exceed the available horizontal space.
     */
    @Override
    protected void layoutRecalculate() {
        FontRenderContext frc = new FontRenderContext(null, true, true);

        Rectangle2D bounds = font.getStringBounds(text, frc);

        int naturalWidth = (int) Math.ceil(bounds.getWidth());
        FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
        int naturalHeight = Math.max((int) Math.ceil(bounds.getHeight()), metrics.getHeight());
        naturalWidth = Math.max(naturalWidth, metrics.stringWidth(text));

        int width = naturalWidth;
        int height = naturalHeight;
        if (parent != null && parent.getWidth() > 0) {
            int availableWidth = parent.getWidth();
            if (!(parent instanceof JFCenter))
                availableWidth = Math.max(0, parent.getWidth() - localX);

            width = Math.min(naturalWidth, availableWidth);
        }

        if (parent != null && parent.getHeight() > 0) {
            int availableHeight = parent.getHeight();
            if (!(parent instanceof JFCenter))
                availableHeight = Math.max(0, parent.getHeight() - localY);

            height = Math.min(naturalHeight, availableHeight);
        }

        setSize(width, height);
    }

    /**
     * Paints the optional background and then draws the text clipped to the component bounds.
     *
     * @param g graphics context used for rendering
     */
    @Override
    protected void design(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        if (backgroundColor != null) {
            g2d.setColor(backgroundColor);
            g2d.fillRect(componentBox.x, componentBox.y, componentBox.width, componentBox.height);
        }

        FontMetrics metrics = g2d.getFontMetrics(font);
        g2d.clipRect(componentBox.x, componentBox.y, componentBox.width, componentBox.height);
        g2d.setColor(color);
        g2d.setFont(font);
        g2d.drawString(text, componentBox.x, componentBox.y + Math.min(metrics.getAscent(), componentBox.height));
        g2d.dispose();
    }

}
