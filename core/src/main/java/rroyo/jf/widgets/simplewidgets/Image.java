package rroyo.jf.widgets.simplewidgets;

import lombok.Getter;
import lombok.Setter;
import rroyo.jf.widgets.basewidgets.Widget;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image extends Widget {

    @Getter
    protected BufferedImage image;

    @Getter
    @Setter
    protected Color bgColor;

    @Getter
    @Setter
    protected boolean preserveAspectRatio = true;

    @Getter
    protected double sizePercentage = 100.0;

    public Image(String imagePath) {
        try {
            this.image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Image(BufferedImage image) {
        this.image = image;
    }

    public Image setImage(BufferedImage image) {
        this.image = image;
        invalidateLayout();
        return this;
    }

    public Image setSizePercentage(double percentage) {
        this.sizePercentage = percentage;
        invalidateLayout();
        return this;
    }

    @Override
    protected void layoutRecalculate() {
        if (image == null) {
            setSize(0, 0);
            return;
        }

        double factor = sizePercentage / 100.0;
        int width = (int) Math.round(image.getWidth() * factor);
        int height = (int) Math.round(image.getHeight() * factor);

        if (!isOverflow() && father != null && father.getSize().width > 0 && father.getSize().height > 0) {
            int maxWidth = Math.max(0, father.getSize().width - local.x);
            int maxHeight = Math.max(0, father.getSize().height - local.y);

            if (width > maxWidth || height > maxHeight) {
                if (preserveAspectRatio) {
                    double scaleX = (double) maxWidth / width;
                    double scaleY = (double) maxHeight / height;

                    double scale = Math.min(scaleX, scaleY);

                    width = (int) Math.floor(width * scale);
                    height = (int) Math.floor(height * scale);
                } else {
                    width = Math.min(width, maxWidth);
                    height = Math.min(height, maxHeight);
                }
            }
        }

        setSize(width, height);
    }

    @Override
    protected void design(Graphics g) {
        if (image == null) return;

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle bounds = getBounds();

        if (bgColor != null) {
            g2d.setColor(bgColor);
            g2d.fillRect(0, 0, bounds.width, bounds.height);
        }

        g2d.drawImage(image, 0, 0, bounds.width, bounds.height, null);

        g2d.dispose();
    }
}