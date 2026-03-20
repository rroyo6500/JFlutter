package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The JFImage class represents a visual component for displaying images
 * with optional configuration for size and background color. This class extends JFComponent,
 * enabling integration into a graphical component hierarchy.
 * <br>
 * JFImage provides multiple constructors for initializing the component either from an existing
 * BufferedImage or by specifying an image file path. It also supports methods to customize
 * the size of the image as a percentage and to set the background color.
 * <br>
 * The component handles rendering of the image and background color using its design method.
 */
public class JFImage extends JFComponent {

    /**
     * Represents the image rendered by the {@code JFImage} component.
     * This field holds the {@code BufferedImage} instance to be displayed,
     * either loaded from a file or provided directly during initialization.
     * <br>
     * The image determines the visual content of the component and is directly
     * utilized in the rendering process in the {@code design} method.
     * <br>
     * This field is immutable and is initialized during object construction.
     */
    private final BufferedImage image;

    /**
     * Represents the background color of the {@code JFImage} component.
     * This field determines the fill color rendered in the component's background area,
     * visible when the component's size exceeds the dimensions of the displayed image
     * or when no image is present.
     * <br>
     * The background color can be set through the {@code setBackgroundColor} method.
     * If no background color is specified, the background area is rendered transparent
     * or with the default color behavior of the graphical component's container.
     */
    private Color backgroundColor;

    /**
     * Constructs a new {@code JFImage} instance with the specified dimensions and image content.
     * This constructor sets the size of the component to the provided width and height,
     * and initializes the image with the given {@code BufferedImage} instance.
     *
     * @param width  the width of the component, in pixels.
     * @param height the height of the component, in pixels.
     * @param image  the {@code BufferedImage} instance to be used as the image content.
     *               Must not be {@code null}.
     */
    public JFImage(int width, int height, @NotNull BufferedImage image) {
        super();
        setSize(width, height);
        this.image = image;
    }

    /**
     * Constructs a new {@code JFImage} instance with the specified dimensions and image content.
     * This constructor sets the size of the component to the provided width and height,
     * and initializes the image by loading it from the specified file path.
     *
     * @param width      the width of the component, in pixels.
     * @param height     the height of the component, in pixels.
     * @param imagePath  the file path of the image to be loaded. Must not be {@code null}.
     *                   If the image cannot be loaded, a {@code RuntimeException} will be thrown.
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
     * Constructs a new {@code JFImage} instance using the specified {@code BufferedImage}.
     * This constructor initializes the internal image to the provided {@code BufferedImage}
     * and sets the size of the component to match the width and height of the image.
     *
     * @param image the {@code BufferedImage} instance to be used as the image content.
     *              Must not be {@code null}.
     */
    public JFImage(@NotNull BufferedImage image) {
        super();
        this.image = image;
        setSize(image.getWidth(), image.getHeight());
    }

    /**
     * Constructs a new {@code JFImage} instance by loading an image from the specified file path.
     * This constructor initializes the internal image using the file path provided and sets the size
     * of the component to match the width and height of the loaded image.
     * If the image cannot be loaded, a {@code RuntimeException} is thrown.
     *
     * @param imagePath the file path of the image to be loaded. Must not be {@code null}.
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
     * Sets the background color for the JFImage instance.
     * This method allows the background color to be customized and updates the
     * internal background color field. The method is chainable, returning the
     * current JFImage instance.
     *
     * @param backgroundColor the new background color to be set. Must be a
     *                        {@code Color} object and not {@code null}.
     * @return the current {@code JFImage} instance with the updated background color.
     */
    public JFImage setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * Updates the size of the image based on the specified percentage of its current dimensions.
     * This method adjusts the width and height of the image proportionally to the given percentage
     * and resizes the image accordingly. The method is chainable, returning the updated {@code JFImage} instance.
     *
     * @param percentage the percentage by which the size of the image will be scaled.
     *                   Must be an integer value greater than 0.
     * @return the updated {@code JFImage} instance with the new size.
     */
    public JFImage setSizePercentage(int percentage) {
        int newWidth = (int) (image.getWidth() * percentage / 100);
        int newHeight = (int) (image.getHeight() * percentage / 100);
        setSize(newWidth, newHeight);
        return this;
    }

    @Override
    public JFComponent addChild(@NotNull JFComponent child) {
        throw new UnsupportedOperationException("JFImage does not support child components");
    }

    @Override
    protected void layoutRecalculate() {

    }

    @Override
    protected void design(Graphics g) {
        if (backgroundColor != null) {
            g.setColor(backgroundColor);
            g.fillRect(componentBox.x, componentBox.y, componentBox.width, componentBox.height);
        }
        g.drawImage(image, componentBox.x, componentBox.y, componentBox.width, componentBox.height, null);


    }

    @Override
    protected void mouseClickAction() {

    }
}
