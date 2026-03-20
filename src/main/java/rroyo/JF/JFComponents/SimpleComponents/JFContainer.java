package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
 */
public class JFContainer extends JFComponent {

    /**
     * Represents the background color of the container.
     * The color is used during rendering to fill the container's bounds.
     * If the color is set to null, the container will not render itself
     * and may issue a warning indicating that it can be replaced by an alternate component.
     */
    private Color color;

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
        this.color = color;
    }

    /**
     * Constructs a new instance of JFContainer with the specified dimensions.
     * This constructor initializes the container's width and height while delegating
     * to a constructor that can optionally accept a background color.
     *
     * @param width  the width of the container in pixels.
     * @param height the height of the container in pixels.
     */
    public JFContainer(int width, int height) {
        this(width, height, null);
    }

    /**
     * Sets the background color of the JFContainer to the specified color.
     * This color is used during the rendering process to fill the container's bounds.
     * If the color is null, the container will not render itself and may issue a warning.
     *
     * @param color the Color object representing the background color to be set for the container;
     *              if null, the container will not render its background.
     * @return the current instance of JFContainer, allowing method chaining.
     */
    public JFContainer setColor(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public JFComponent addChild(@NotNull JFComponent child) {
        childList.clear();
        super.addChild(child);
        return this;
    }

    @Override
    protected void layoutRecalculate() {

    }

    @Override
    protected void design(Graphics g) {
        if (color == null) {
            System.err.printf(
                    "Warning: Component [JFContainer(%d, %d, null)] can be replaced by 'JFSizedBox'%n",
                    componentBox.width, componentBox.height
            );
            return;
        }

        g.setColor(color);
        g.fillRect(componentBox.x, componentBox.y, componentBox.width, componentBox.height);
    }
}
