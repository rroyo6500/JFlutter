package CustomComponents.TicTacToe;

import rroyo.JF.Decorations.Border;
import rroyo.JF.Enums.Alignment;
import rroyo.JF.JFComponents.BaseComponent.JFComplexComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFContainer;
import rroyo.JF.JFComponents.SimpleComponents.JFImage;
import rroyo.JF.JFComponents.SimpleComponents.JFStack;
import rroyo.JF.JFEvents.JFInteractiveComponent;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TTTBox extends JFComplexComponent implements JFInteractiveComponent {

    private char value = ' ';

    private final JFContainer container;
    private final JFImage xImage;
    private final JFImage oImage;

    public TTTBox(int width, int height, String xImagePath, String oImagePath) {
        this(
                new JFContainer(width, height, Color.lightGray),
                new JFImage(xImagePath),
                new JFImage(oImagePath)
        );
    }

    private TTTBox(JFContainer container, JFImage xImage, JFImage oImage) {
        super(() -> {

            xImage.setSize(container.getWidth(), container.getHeight()).setVisible(false);
            oImage.setSize(container.getWidth(), container.getHeight()).setVisible(false);

            container.addChild(
                    new JFStack(Alignment.CENTER).addChilds(
                            xImage,
                            oImage
                    )
            );
            container.getDecoration().setBorder(new Border(Color.black, 2));

            return container;
        });
        this.container = container;
        this.xImage = xImage;
        this.oImage = oImage;
    }

    public char getValue() {
        return value;
    }

    public boolean setValue(char value) {
        if (getValue() != ' ' && Character.toLowerCase(value) == 'x' || Character.toLowerCase(value) == 'o') {
            switch (Character.toLowerCase(value)) {
                case 'x' -> xImage.setVisible(true);
                case 'o' -> oImage.setVisible(true);
            }
            this.value = value;
            return true;
        }
        return false;
    }

    public JFContainer getContainer() {
        return container;
    }

    public JFImage getxImage() {
        return xImage;
    }

    public JFImage getoImage() {
        return oImage;
    }

}
