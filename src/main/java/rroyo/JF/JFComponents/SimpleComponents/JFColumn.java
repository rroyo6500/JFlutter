package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;

import java.awt.*;

/**
 * Flex container that arranges children vertically from top to bottom.
 * <p>
 * The column is the vertical counterpart of {@link JFRow}. It measures child heights,
 * computes its own height according to parent context, and then positions children on the
 * Y axis while optionally aligning them on the X axis.
 *
 * @author rroyo
 */
public class JFColumn extends JFFlex {

    /**
     * Creates a column initialized with the supplied children.
     *
     * @param children children to place from top to bottom
     */
    public JFColumn(@NotNull JFComponent... children) {
        super();
        addChilds(children);
    }

    /**
     * Creates an empty column.
     */
    public JFColumn() {
        super();
    }

    /**
     * Recomputes column size and child positions.
     * <p>
     * The column first measures the sum of child heights and the maximum child width. It then
     * chooses whether to wrap content vertically or stretch to ancestor constraints. Once its
     * final height is known, it distributes remaining free space according to the configured
     * main-axis alignment and positions each child.
     */
    @Override
    protected void layoutRecalculate() {

        int totalChildrenHeight = 0;
        int maxChildWidth = 0;
        int activeChildCount = 0;

        for (JFComponent child : childList) {
            if (!child.isActive()) continue;

            totalChildrenHeight += child.getHeight();
            maxChildWidth = Math.max(maxChildWidth, child.getWidth());
            activeChildCount++;
        }

        int finalHeight = totalChildrenHeight;
        if (parent != null) {
            finalHeight = (parent.getHeight() > 0)
                    ? (parent.getClass() == this.getClass())
                        ? totalChildrenHeight
                        : parent.getHeight()
                    : (parent.getClass() == JFCenter.class)
                        ? getComponentFromTree(JFContainer.class, JFSizedBox.class).getHeight()
                        : totalChildrenHeight;
        }

        setSize(maxChildWidth, finalHeight);

        int remainingSpace = componentBox.height - totalChildrenHeight;
        float[] fChildPos = calculateFlexChildPositions(remainingSpace, activeChildCount);

        float currentY = fChildPos[0];
        float gap = fChildPos[1];

        for (JFComponent child : childList) {
            if (!child.isActive()) continue;

            int childX = switch (caa) {
                case CENTER -> (componentBox.width - child.getWidth()) / 2;
                case END -> componentBox.width - child.getWidth();
                default -> 0;
            };

            child.setPosition(childX, (int) currentY);
            currentY += child.getHeight() + gap;
        }

    }

    /**
     * Columns do not paint anything by themselves; they only arrange their children.
     *
     * @param g graphics context supplied during the paint pass
     */
    @Override
    protected void design(Graphics g) {

    }

}
