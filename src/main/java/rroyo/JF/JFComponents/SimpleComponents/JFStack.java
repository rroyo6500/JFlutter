package rroyo.JF.JFComponents.SimpleComponents;

import rroyo.JF.Enums.Alignment;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

/**
 * Layered container that stacks children using a shared bounding area.
 *
 * @author rroyo
 */
public class JFStack extends JFComponent {

    /**
     * Preferred alignment reference for stacked children.
     */
    private Alignment alignment;

    /**
     * Creates a stack component with the requested alignment strategy.
     *
     * @param alignment alignment mode used by the stack layout
     */
    public JFStack(Alignment alignment) {
        super(true);
        this.alignment = alignment;
    }

    @Override
    protected void layoutRecalculate() {
        setSize(parent.getWidth(), parent.getHeight());
    }

    @Override
    protected void design(Graphics g) {



    }
}
