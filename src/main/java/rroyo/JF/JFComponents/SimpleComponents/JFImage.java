package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Visual component that renders a bitmap image inside the component tree.
 * <p>
 * The image can be created from an existing {@link BufferedImage} or loaded from disk using a
 * path. The component supports explicit sizing, proportional resizing by percentage, and an
 * optional background fill behind the image.
 *
 * @author rroyo
 */
public class JFImage extends JFComponent {

    /**
     * Image content rendered by the component.
     */
    private final BufferedImage image;

    /**
     * Optional background color painted before the image itself.
     */
    private Color backgroundColor;

    /**
     * Creates an image component with explicit size using an already loaded image.
     *
     * @param width target width in pixels
     * @param height target height in pixels
     * @param image image content to render
     */
    public JFImage(int width, int height, @NotNull BufferedImage image) {
        super();
        setSize(width, height);
        this.image = image;
    }

    /**
     * Creates an image component with explicit size by loading the image from disk.
     *
     * @param width target width in pixels
     * @param height target height in pixels
     * @param imagePath path of the image file to load
     */
    public JFImage(int width, int height, @NotNull String imagePath) {
        super();
        setSize(width, height);
        try {
            this.image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates an image component sized to the natural dimensions of the supplied image.
     *
     * @param image image content to render
     */
    public JFImage(@NotNull BufferedImage image) {
        super();
        this.image = image;
        setSize(image.getWidth(), image.getHeight());
    }

    /**
     * Creates an image component by loading an image from disk and adopting its natural size.
     *
     * @param imagePath path of the image file to load
     */
    public JFImage(@NotNull String imagePath) {
        super();
        try {
            this.image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setSize(image.getWidth(), image.getHeight());
    }

    /**
     * Sets the background fill color painted behind the image.
     *
     * @param backgroundColor background color to use
     * @return current image component
     */
    public JFImage setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * Resizes the component to a percentage of the natural image size.
     *
     * @param percentage percentage of the original width and height
     * @return current image component
     */
    public JFImage setSizePercentage(int percentage) {
        int newWidth = image.getWidth() * percentage / 100;
        int newHeight = image.getHeight() * percentage / 100;
        setSize(newWidth, newHeight);
        return this;
    }

    /**
     * Image size is already explicitly known, so no additional layout work is required.
     */
    @Override
    protected void layoutRecalculate() {

    }

    /**
     * Paints the optional background and then draws the image stretched to the component bounds.
     *
     * @param g graphics context used for rendering
     */
    @Override
    protected void design(Graphics g) {
        if (backgroundColor != null) {
            g.setColor(backgroundColor);
            g.fillRect(componentBox.x, componentBox.y, componentBox.width, componentBox.height);
        }
        g.drawImage(image, componentBox.x, componentBox.y, componentBox.width, componentBox.height, null);
    }

}
