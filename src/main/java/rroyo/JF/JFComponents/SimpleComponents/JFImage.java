package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JFImage extends JFComponent {

    private final BufferedImage image;
    private Color backgroundColor;

    public JFImage(int width, int height, @NotNull BufferedImage image) {
        super();
        setSize(width, height);
        this.image = image;
    }
    public JFImage(int width, int height, @NotNull String imagePath) {
        super();
        setSize(width, height);
        try {
            this.image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public JFImage(@NotNull BufferedImage image) {
        super();
        this.image = image;
        setSize(image.getWidth(), image.getHeight());
    }
    public JFImage(@NotNull String imagePath) {
        super();
        try {
            this.image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setSize(image.getWidth(), image.getHeight());
    }

    public JFImage setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public JFImage setSizePercentage(int percentage) {
        int newWidth = (int) (image.getWidth() * percentage / 100);
        int newHeight = (int) (image.getHeight() * percentage / 100);
        setSize(newWidth, newHeight);
        return this;
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
}
