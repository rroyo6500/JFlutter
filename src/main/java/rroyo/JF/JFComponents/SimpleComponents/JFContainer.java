package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rroyo.JF.Decorations.Decoration;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

/**
 * JFContainer is a specialized component that extends the functionality of JFComponent.
 * It serves as a container that can encapsulate other components and provides additional
 * rendering features such as background coloring.
 * <br>
 * Instances of JFContainer can be customized using their dimensions and optional background color.
 * The container's layout and child components are managed according to the defined layout rules
 * and hierarchy of JFComponent.
 *
 * @author rroyo
 */
public class JFContainer extends JFComponent {

    /**
     * Represents the visual decoration of the container, providing customization
     * options such as background color and border styling. This field defines how
     * the container's surface is visually rendered and allows inclusion of additional
     * visual elements to enhance its appearance.
     * <br>
     * The decoration may include, but is not limited to, a background color for the
     * container and an optional border. It is used during rendering to define the
     * graphical representation of the container.
     */
    private final Decoration decoration;

    /**
     * Constructs a new instance of JFContainer with the specified dimensions and an optional background color.
     * The width and height define the size of the container, while the background color determines the
     * rendering fill color of the container. If the color is null, no background will be rendered.
     *
     * @param width  the width of the container in pixels.
     * @param height the height of the container in pixels.
     * @param color  the optional background color of the container. If null, the container
     *               will not render a background.
     */
    public JFContainer(int width, int height, @Nullable Color color) {
        setSize(width, height);
        this.decoration = new Decoration(color);
    }

    /**
     * Constructs a new instance of JFContainer with the specified dimensions and an optional decoration.
     * This constructor allows setting the width and height of the container and optionally applying a decoration
     * that can be used to customize the visual appearance of the container.
     *
     * @param width      the width of the container in pixels.
     * @param height     the height of the container in pixels.
     * @param decoration the decoration for this container, which may include visual elements such as borders
     *                   or background color. If null, no decoration will be applied.
     */
    public JFContainer(int width, int height, @NotNull Decoration decoration) {
        setSize(width, height);
        this.decoration = decoration;
    }

    /**
     * Returns the decoration object used to render this container.
     *
     * @return container decoration configuration
     */
    public Decoration getDecoration() {
        return decoration;
    }

    @Override
    public JFContainer setSize(int width, int height) {
        return (JFContainer) super.setSize(width, height);
    }

    @Override
    public JFContainer addChild(@NotNull JFComponent child) {
        childList.clear();
        super.addChild(child);
        return this;
    }

    @Override
    protected void layoutRecalculate() {

    }

    @Override
    protected void design(Graphics g) {
        if (decoration.getColor() == null) {
            System.err.printf(
                    "Warning: Component [JFContainer(%d, %d, null)] can be replaced by 'JFSizedBox'%n",
                    componentBox.width, componentBox.height
            );
            return;
        }

        decoration.draw(g, componentBox.x, componentBox.y, componentBox.width, componentBox.height);

    }

}
