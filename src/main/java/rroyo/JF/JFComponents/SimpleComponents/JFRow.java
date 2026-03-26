package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

/**
 * JFRow is a specific implementation of JFFlex designed to arrange its child components
 * in a horizontal row layout. It manages the positioning and spacing of its children
 * along the main horizontal axis, adhering to flexbox-style alignment principles.
 * <br>
 * JFRow ensures that invalid components, such as JFCenter, cannot be added as children
 * to maintain valid layout behavior.
 * <br>
 * The class supports layout recalculation and rendering, ensuring that child components
 * are aligned correctly based on the main axis and cross axis alignment settings inherited
 * from JFFlex.
 */
public class JFRow extends JFFlex {

    /**
     * Constructs a JFRow instance containing the specified child components.
     * JFRow arranges its child components in a horizontal row layout, aligning
     * them based on the flexbox-style alignment rules inherited from JFFlex.
     *
     * @param children an array of JFComponent objects to be added as children
     *                 of this JFRow. Each child will be added and arranged
     *                 horizontally. If one of the components is an instance of
     *                 JFCenter, an IllegalArgumentException will be thrown as
     *                 JFCenter is not a valid child for a JFRow.
     */
    public JFRow(@NotNull JFComponent... children) {
        super();
        addChilds(children);
    }

    /**
     * Creates an instance of JFRow.
     * <br>
     * This default constructor initializes a JFRow instance with no child components.
     * JFRow extends JFFlex and organizes its child components in a horizontal row layout.
     * By default, it adopts the alignment configurations provided by the JFFlex superclass.
     */
    public JFRow() {
        super();
    }

    /**
     * Adds the specified child components to this JFRow instance.
     * JFRow arranges its child components in a horizontal layout.
     * <br>
     * Any attempt to add a JFCenter instance as a child will result in
     * an IllegalArgumentException, since JFCenter is not a valid child
     * component for JFRow.
     *
     * @param children the array of JFComponent objects to be added as
     *                 children to this JFRow. Each child is validated and
     *                 added to the layout. If a child of type JFCenter is
     *                 encountered, an exception is thrown.
     *
     * @throws IllegalArgumentException if any child in the provided array
     *                                  is an instance of JFCenter.
     */
    private void addChilds(@NotNull JFComponent... children) {
        for (JFComponent child : children) {

            if (child.getClass() == JFCenter.class)
                throw new IllegalArgumentException("Error: Cannot add JFCenter in to a Row");

            super.addChild(child);
        }
    }

    @Override
    protected void layoutRecalculate() {

        int totalChildrenWidth = 0;
        int maxChildHeight = 0;

        for (JFComponent child : childList) {
            totalChildrenWidth += child.componentBox.width;
            maxChildHeight = Math.max(maxChildHeight, child.componentBox.height);
        }

        int finalWidth = totalChildrenWidth;
        if (parent != null) {
            finalWidth = (parent.componentBox.width > 0)
                    ? (parent.getClass() == this.getClass())
                        ? totalChildrenWidth
                        : parent.componentBox.width
                    : (parent.getClass() == JFCenter.class)
                        ? getComponentFromTree(JFContainer.class, JFSizedBox.class).componentBox.width
                        : totalChildrenWidth
            ;
        }

        setSize(finalWidth, maxChildHeight);

        int remainingSpace = componentBox.width - totalChildrenWidth;
        int childCount = childList.size();

        float[] fChildPos = calculateFlexChildPositions(remainingSpace, childCount);

        float currentX = fChildPos[0];
        float gap = fChildPos[1];

        for (JFComponent child : childList) {
            int childY = switch (caa) {
                case CENTER -> (componentBox.height - child.componentBox.height) / 2;
                case END -> componentBox.height - child.componentBox.height;
                default -> 0;
            };

            child.setPosition((int) currentX, childY);
            currentX += child.componentBox.width + gap;
        }

    }

    @Override
    protected void design(Graphics g) {

    }

}
