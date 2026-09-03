package rroyo.jf.widgets.complexwidgets;

import rroyo.jf.annotations.JFWidget;
import rroyo.jf.enums.Alignment;
import rroyo.jf.widgets.basewidgets.ComplexWidget;
import rroyo.jf.widgets.basewidgets.Widget;
import rroyo.jf.widgets.childcomponent.SingleChildComponent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static rroyo.jf.generated.Widgets.*;

@JFWidget(name = "Image")
public class SizedImage extends ComplexWidget {

    private final BufferedImage image;
    private final Dimension size;

    public SizedImage(String imagePath, int width, int height) {
        try {
            this.size = new Dimension(width, height);
            this.image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SizedImage(BufferedImage image, int width, int height) {
        this.size = new Dimension(width, height);
        this.image = image;
    }

    public SizedImage(String imagePath, Dimension size) {
        try {
            this.size = size;
            this.image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SizedImage(BufferedImage image, Dimension size) {
        this.size = size;
        this.image = image;
    }

    @Override
    protected Widget build() {
        return SizedBox(size)
                .addChild(
                        Center().addChild(
                                        Image(image)
                                )
                );
    }

}
