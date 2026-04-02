package rroyo.JF.JFComponents.SimpleComponents;

import java.awt.*;

/**
 * Optional drawing hook rendered directly by a {@link JFWindow} after the component tree.
 * <p>
 * A canvas is useful for ad-hoc debug overlays, custom effects, or experiments that do not
 * naturally fit into the regular component hierarchy but still need access to the window
 * graphics context.
 *
 * @author rroyo
 */
public abstract class JFCanvas {

    /**
     * Draws custom content using the provided graphics context.
     *
     * @param g graphics context used for rendering
     */
    protected abstract void draw(Graphics g);

}
