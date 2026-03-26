package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

/**
 * Lightweight drawing hook that allows custom paint logic over a window surface.
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
