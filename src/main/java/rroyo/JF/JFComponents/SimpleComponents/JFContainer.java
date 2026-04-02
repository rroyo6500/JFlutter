package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rroyo.JF.Decorations.Decoration;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;
import rroyo.JF.JFComponents.BaseComponent.JFSingleChildComponent;

import java.awt.*;

/**
 * Fixed-size container that can paint a background, border and shadow through a {@link Decoration}.
 * <p>
 * The container is one of the main visual building blocks of the framework. It can wrap a single
 * child and render a decorated box behind it, which makes it useful for cards, panels, badges and
 * many other UI elements.
 *
 * @author rroyo
 */
public class JFContainer extends JFComponent implements JFSingleChildComponent<JFContainer> {

    /**
     * Decoration responsible for painting the visual surface of the container.
     */
    private final Decoration decoration;

    /**
     * Creates a decorated container from width, height and a plain background color.
     *
     * @param width container width in pixels
     * @param height container height in pixels
     * @param color optional background color; when {@code null}, no fill is painted
     */
    public JFContainer(int width, int height, @Nullable Color color) {
        setSize(width, height);
        this.decoration = new Decoration(color);
    }

    /**
     * Creates a container using a fully configured decoration object.
     *
     * @param width container width in pixels
     * @param height container height in pixels
     * @param decoration decoration used to render the container surface
     */
    public JFContainer(int width, int height, @NotNull Decoration decoration) {
        setSize(width, height);
        this.decoration = decoration;
    }

    /**
     * Returns the decoration currently used to paint the container.
     *
     * @return decoration instance associated with the container
     */
    public Decoration getDecoration() {
        return decoration;
    }

    /**
     * Narrows the return type of {@link JFComponent#setSize(int, int)} for fluent use.
     *
     * @param width new width in pixels
     * @param height new height in pixels
     * @return current container
     */
    @Override
    public JFContainer setSize(int width, int height) {
        return (JFContainer) super.setSize(width, height);
    }

    /**
     * Replaces any previous child so the container behaves as a single-child box.
     *
     * @param child child component to mount inside the container
     * @return current container
     */
    @Override
    public JFContainer addChild(@NotNull JFComponent child) {
        clearChildren();
        attachChild(child);
        return this;
    }

    /**
     * The container uses the fixed size supplied at construction time, so it performs no extra
     * layout calculations of its own.
     */
    @Override
    protected void layoutRecalculate() {

    }

    /**
     * Paints the configured decoration across the full bounds of the container.
     * <p>
     * When the decoration has no background color, a warning is printed because a plain
     * {@link JFSizedBox} would usually be a more appropriate choice.
     *
     * @param g graphics context used for rendering
     */
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
