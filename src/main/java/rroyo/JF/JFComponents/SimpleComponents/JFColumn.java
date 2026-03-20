package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.Enums.CrossAxisAlignment;
import rroyo.JF.JFComponents.Enums.MainAxisAlignment;
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

    /**
     * Adds the specified child components to the column layout.
     * This method appends the provided children to the column, ensuring that
     * no instance of {@code JFCenter} is added, as it is not supported within a column.
     * Attempting to add {@code JFCenter} will throw an {@link IllegalArgumentException}.
     *
     * @param children the array of {@link JFComponent} instances to be added as children
     *                 of this column. All provided components will be appended to the column
     *                 layout in their respective order. Cannot contain instances of {@code JFCenter}.
     * @return the current {@link JFColumn} instance, allowing for method chaining.
     * @throws IllegalArgumentException if one of the specified children is an instance of {@code JFCenter}.
     */
    public JFColumn addChilds(@NotNull JFComponent... children) {
        for (JFComponent child : children) {

            if (child.getClass() == JFCenter.class)
                throw new IllegalArgumentException("Error: Cannot add JFCenter in to a Column");

            super.addChild(child);
        }
        return this;
    }

    @Override
    protected void layoutRecalculate() {

        int totalChildrenHeight = 0;
        int maxChildWidth = 0;

        for (JFComponent child : childList) {
            totalChildrenHeight += child.componentBox.height;
            maxChildWidth = Math.max(maxChildWidth, child.componentBox.width);
        }

        int finalHeight = totalChildrenHeight;
        if (parent != null) {
            finalHeight = (parent.componentBox.height > 0)
                    ? parent.componentBox.height
                    : (parent.getClass() == JFCenter.class)
                        ? getComponentFromTree(JFContainer.class, JFSizedBox.class).componentBox.height
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
                case CENTER -> (componentBox.width - child.componentBox.width) / 2;
                case END -> componentBox.width - child.componentBox.width;
                default -> 0;
            };

            child.setPosition(childX, (int) currentY);
            currentY += child.componentBox.height + gap;
        }

    }

    @Override
    protected void design(Graphics g) {

    }

    @Override
    protected void mouseClickAction() {

    }
}
