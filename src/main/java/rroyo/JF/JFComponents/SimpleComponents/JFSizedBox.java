package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;
import rroyo.JF.JFComponents.BaseComponent.JFSingleChildComponent;

import java.awt.*;

/**
 * Minimal fixed-size box that can optionally host a single child.
 * <p>
 * The component does not paint anything by itself. Its main purpose is to reserve space or to
 * impose a fixed-size constraint on a wrapped child.
 *
 * @author rroyo
 */
public class JFSizedBox extends JFComponent implements JFSingleChildComponent<JFSizedBox> {

    /**
     * Creates a fixed-size box with the requested dimensions.
     *
     * @param width fixed width in pixels
     * @param height fixed height in pixels
     */
    public JFSizedBox(int width, int height) {
        setSize(width, height);
    }

    /**
     * Replaces any previous child so the size box behaves as a single-child wrapper.
     *
     * @param child child to host inside the fixed-size box
     * @return current size box
     */
    @Override
    public JFSizedBox addChild(@NotNull JFComponent child) {
        clearChildren();
        attachChild(child);
        return this;
    }

    /**
     * The box keeps the explicit dimensions configured by the caller, so it performs no extra layout.
     */
    @Override
    protected void layoutRecalculate() {

    }

    /**
     * The size box is visually transparent and only contributes layout constraints.
     *
     * @param g graphics context supplied during the paint pass
     */
    @Override
    protected void design(Graphics g) {

    }

}
