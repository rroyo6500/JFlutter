package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

/**
 * The JFSizedBox class is an extension of the JFComponent class that provides
 * a container with a fixed width and height. It is designed to hold a single
 * child component, removing any previously added child before adding a new one.
 * This class is typically used to enforce a specific size constraint for the
 * contained component.
 */
public class JFSizedBox extends JFComponent {

    /**
     * Constructs a JFSizedBox instance with the specified width and height.
     * This constructor initializes the size of the component by setting its
     * width and height dimensions using the provided values.
     *
     * @param width  the width of the JFSizedBox, in pixels.
     * @param height the height of the JFSizedBox, in pixels.
     */
    public JFSizedBox(int width, int height) {
        setSize(width, height);
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

    }

}
