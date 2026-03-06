package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

public class JFColumn extends JFComponent {

    public enum mainAxisAlignment {
        DEFAULT,
        START,
        CENTER,
        END,
        SPACE_BETWEEN,
        SPACE_AROUND,
        SPACE_EVENLY
    }
    public enum crossAxisAlignment {
        DEFAULT,
        START,
        CENTER,
        END
    }

    public mainAxisAlignment maa = mainAxisAlignment.DEFAULT;
    public crossAxisAlignment caa = crossAxisAlignment.DEFAULT;

    public JFColumn(@NotNull JFComponent... children) {
        super(true);
        addChilds(children);
    }
    public JFColumn() {
        super(true);
    }

    public JFColumn mainAxisAlignment(mainAxisAlignment maa) {
        this.maa = maa;
        return this;
    }
    public JFColumn crossAxisAlignment(crossAxisAlignment caa) {
        this.caa = caa;
        return this;
    }

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

        float currentY = 0;
        float gap = 0;

        if (childCount > 0) {
            switch (maa) {
                case START, DEFAULT -> { currentY = 0; gap = 0; }
                case CENTER -> { currentY = remainingSpace / 2f; gap = 0; }
                case END -> { currentY = remainingSpace; gap = 0; }

                case SPACE_BETWEEN -> {
                    currentY = 0;
                    gap = (childCount > 1) ? (float)remainingSpace / (childCount - 1) : 0;
                }
                case SPACE_AROUND -> {
                    gap = (float)remainingSpace / childCount;
                    currentY = gap / 2f;
                }
                case SPACE_EVENLY -> {
                    gap = (float)remainingSpace / (childCount + 1);
                    currentY = gap;
                }
            }
        }

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
        /*
        g.setColor(Color.gray);
        g.fillRect(componentBox.x, componentBox.y, componentBox.width, componentBox.height);
        // */
    }
}
