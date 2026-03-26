package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

/**
 * The JFColumn class represents a vertical column layout container that organizes
 * child components in a sequential, vertical arrangement. It extends the JFFlex
 * class to inherit flexbox functionality such as alignment, spacing, and size
 * management. This class is specifically designed to manage components that are
 * arranged vertically within the layout.
 * <br>
 * JFColumn prohibits the inclusion of {@code JFCenter} instances as child components.
 * Attempts to add a {@code JFCenter} as a child will result in an IllegalArgumentException.
 * The class provides constructors to initialize the column with or without children,
 * and methods for dynamically adding children to the layout.
 * <br>
 * JFColumn ensures child positions and sizes are recalculated dynamically to
 * adhere to the layout's constraints and configuration. Additionally, visual representation
 * aspects such as drawing and alignment configurations are managed through overridden
 * methods from the superclass, JFFlex.
 */
public class JFColumn extends JFFlex {

    /**
     * Constructs a new JFColumn with the specified children. This constructor initializes
     * a vertical column layout containing the provided children components.
     *
     * @param children the array of {@link JFComponent} instances to be added as children
     *                 of this column. All components will be arranged in a vertical sequence.
     *                 Cannot contain instances of {@code JFCenter}, otherwise an
     *                 {@link IllegalArgumentException} will be thrown.
     */
    public JFColumn(@NotNull JFComponent... children) {
        super();
        addChilds(children);
    }

    /**
     * Constructs a new instance of JFColumn.
     * <br>
     * This default constructor initializes a vertical column layout without any child components.
     * It extends the functionality of the JFFlex superclass, which provides fundamental methods
     * for managing flexbox-style layouts. JFColumn organizes its child components along the vertical axis
     * and adheres to the alignment configurations provided by JFFlex.
     */
    public JFColumn() {
        super();
    }

    @Override
    protected void layoutRecalculate() {

        int totalChildrenHeight = 0;
        int maxChildWidth = 0;

        for (JFComponent child : childList) {
            totalChildrenHeight += child.getHeight();
            maxChildWidth = Math.max(maxChildWidth, child.getWidth());
        }

        int finalHeight = totalChildrenHeight;
        if (parent != null) {
            finalHeight = (parent.getHeight() > 0)
                    ? (parent.getClass() == this.getClass())
                        ? totalChildrenHeight
                        : parent.getHeight()
                    : (parent.getClass() == JFCenter.class)
                        ? getComponentFromTree(JFContainer.class, JFSizedBox.class).getHeight()
                        : totalChildrenHeight
            ;
        }

        setSize(maxChildWidth, finalHeight);

        int remainingSpace = componentBox.height - totalChildrenHeight;
        int childCount = childList.size();

        float[] fChildPos = calculateFlexChildPositions(remainingSpace, childCount);

        float currentY = fChildPos[0];
        float gap = fChildPos[1];

        for (JFComponent child : childList) {
            int childX = switch (caa) {
                case CENTER -> (componentBox.width - child.getWidth()) / 2;
                case END -> componentBox.width - child.getWidth();
                default -> 0;
            };

            child.setPosition(childX, (int) currentY);
            currentY += child.getHeight() + gap;
        }

    }

    @Override
    protected void design(Graphics g) {

    }

}
