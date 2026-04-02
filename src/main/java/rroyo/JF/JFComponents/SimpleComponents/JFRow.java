package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;

import java.awt.*;

/**
 * Flex container that arranges children horizontally from left to right.
 * <p>
 * The row computes the total width occupied by its children, chooses its own width depending
 * on parent context, and then positions each child along the X axis according to the selected
 * main-axis and cross-axis alignment rules inherited from {@link JFFlex}.
 *
 * @author rroyo
 */
public class JFRow extends JFFlex {

    /**
     * Creates a row initialized with the supplied children.
     *
     * @param children children to place from left to right
     */
    public JFRow(@NotNull JFComponent... children) {
        super();
        addChilds(children);
    }

    /**
     * Creates an empty row.
     */
    public JFRow() {
        super();
    }

    /**
     * Recomputes row size and child positions.
     * <p>
     * The row first measures the sum of child widths and the maximum child height. It then decides
     * its own width: it may wrap its content, stretch to the parent width, or adopt a constrained
     * width from an ancestor used by centering helpers. Once its final width is known, it distributes
     * any remaining free space according to the configured main-axis alignment and positions each child.
     */
    @Override
    protected void layoutRecalculate() {

        int totalChildrenWidth = 0;
        int maxChildHeight = 0;
        int activeChildCount = 0;

        for (JFComponent child : childList) {
            if (!child.isActive()) continue;

            totalChildrenWidth += child.getWidth();
            maxChildHeight = Math.max(maxChildHeight, child.getHeight());
            activeChildCount++;
        }

        int finalWidth = totalChildrenWidth;
        if (parent != null) {
            finalWidth = (parent.getWidth() > 0)
                    ? (parent.getClass() == this.getClass())
                        ? totalChildrenWidth
                        : parent.getWidth()
                    : (parent.getClass() == JFCenter.class)
                        ? getComponentFromTree(JFContainer.class, JFSizedBox.class).getWidth()
                        : totalChildrenWidth;
        }

        setSize(finalWidth, maxChildHeight);

        int remainingSpace = componentBox.width - totalChildrenWidth;
        float[] fChildPos = calculateFlexChildPositions(remainingSpace, activeChildCount);

        float currentX = fChildPos[0];
        float gap = fChildPos[1];

        for (JFComponent child : childList) {
            if (!child.isActive()) continue;

            int childY = switch (caa) {
                case CENTER -> (componentBox.height - child.getHeight()) / 2;
                case END -> componentBox.height - child.getHeight();
                default -> 0;
            };

            child.setPosition((int) currentX, childY);
            currentX += child.getWidth() + gap;
        }

    }

    /**
     * Rows do not draw any decoration of their own; only their children are rendered.
     *
     * @param g graphics context supplied during the paint pass
     */
    @Override
    protected void design(Graphics g) {

    }

}
