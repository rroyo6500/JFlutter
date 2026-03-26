package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

/**
 * The JFText class is a UI component that represents a text element
 * with customizable font, text color, and background color. It extends
 * the JFComponent class and provides methods to set the text and its styling.
 * This class also handles the layout recalculations and rendering of the
 * text on the screen.
 *
 * @author rroyo
 */
public class JFText extends JFComponent {

    /**
     * Represents the text content of the JFText component.
     * This string determines the text to be rendered on screen.
     * It can be set or updated to change the displayed content
     * dynamically and is used during layout recalculation and rendering.
     */
    private String text;

    /**
     * Represents the default font used for rendering text within the JFText component.
     * The font is initialized with the Arial typeface, a plain style, and a size of 12.
     *
     * This font can be customized dynamically using the {@code setFont(Font font)} method
     * in the JFText class to modify the appearance of the text displayed in the component.
     */
    private Font font = new Font("Arial", Font.PLAIN, 12);

    /**
     * Represents the text color used for rendering the text in the {@code JFText} component.
     * By default, it is initialized to {@code Color.BLACK}, ensuring the text is displayed
     * in a standard black color. The color can be dynamically updated using the
     * {@code setColor(Color color)} method to customize the text's appearance.
     */
    private Color color = Color.BLACK;

    /**
     * Represents the background color of the {@code JFText} component.
     * This color is used to fill the background of the component when it is rendered.
     *
     * By default, this variable is initialized to {@code null}, meaning no background
     * color is applied. It can be dynamically updated using the {@code setBackgroundColor(Color backgroundColor)}
     * method provided by the {@code JFText} class to modify the appearance of the component.
     *
     * If the background color is set, it is drawn within the bounds defined by the
     * component's size during the rendering process.
     */
    private Color backgroundColor;

    /**
     * Constructs a JFText instance with the specified text content.
     *
     * @param text the string value to be displayed or represented by this JFText instance.
     */
    public JFText(String text) {
        this.text = text;
    }

    /**
     * Updates the text content of this JFText instance.
     *
     * @param text the new text string to be set. This value will replace the
     *             current text content of the instance.
     * @return the current JFText instance, allowing for method chaining.
     */
    public JFText setText(String text) {
        this.text = text;
        invalidateLayout();
        return this;
    }

    /**
     * Sets the font for this JFText instance.
     *
     * @param font the Font object to set. This defines the typeface, style, and size
     *             to be used for displaying the text content of this JFText instance.
     * @return the current JFText instance, allowing for method chaining.
     */
    public JFText setFont(Font font) {
        this.font = font;
        invalidateLayout();
        return this;
    }

    /**
     * Sets the color for this JFText instance.
     *
     * @param color the Color object to set. This determines the color
     *              to be used for rendering the text content of this JFText instance.
     * @return the current JFText instance, allowing for method chaining.
     */
    public JFText setColor(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public JFComponent addChild(@NotNull JFComponent child) {
        throw new UnsupportedOperationException("JFText does not support child components");
    }

    /**
     * Sets the background color for this JFText instance.
     * This determines the background color of the text component.
     *
     * @param backgroundColor the Color object to set as the background color
     *                        of this JFText instance.
     * @return the current JFText instance, allowing for method chaining.
     */
    public JFText setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    @Override
    protected void layoutRecalculate() {
        FontRenderContext frc = new FontRenderContext(null, true, true);

        Rectangle2D bounds = font.getStringBounds(text, frc);

        int naturalWidth = (int) Math.ceil(bounds.getWidth());
        int height = (int) Math.ceil(bounds.getHeight());
        FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
        naturalWidth = Math.max(naturalWidth, metrics.stringWidth(text));

        int width = naturalWidth;
        if (parent != null && parent.getWidth() > 0) {
            int availableWidth = parent.getWidth();
            if (!(parent instanceof JFCenter))
                availableWidth = Math.max(0, parent.getWidth() - localX);

            width = Math.min(naturalWidth, availableWidth);
        }

        setSize(width, height);
    }

    @Override
    protected void design(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        if (backgroundColor != null) {
            g2d.setColor(backgroundColor);
            g2d.fillRect(componentBox.x, componentBox.y, componentBox.width, componentBox.height);
        }

        g2d.setClip(componentBox.x, componentBox.y, componentBox.width, componentBox.height);
        g2d.setColor(color);
        g2d.setFont(font);
        g2d.drawString(text, componentBox.x, componentBox.y + componentBox.height);
        g2d.dispose();
    }

}
