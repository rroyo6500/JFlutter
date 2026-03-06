package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

public class JFRow extends JFComponent {

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

    public JFRow(@NotNull JFComponent... children) {
        super(true);
        addChilds(children);
    }
    public JFRow() {
        super(true);
    }

    public JFRow mainAxisAlignment(mainAxisAlignment maa) {
        this.maa = maa;
        return this;
    }
    public JFRow crossAxisAlignment(crossAxisAlignment caa) {
        this.caa = caa;
        return this;
    }

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
                    ? parent.componentBox.width
                    : (parent.getClass() == JFCenter.class)
                        ? getComponentFromTree(JFContainer.class, JFSizedBox.class).componentBox.width
                        : totalChildrenWidth
            ;
        }

        setSize(finalWidth, maxChildHeight);

        int remainingSpace = componentBox.width - totalChildrenWidth;
        int childCount = childList.size();

        float currentX = 0;
        float gap = 0;

        if (childCount > 0) {
            switch (maa) {
                case START, DEFAULT -> { currentX = 0; gap = 0; }
                case CENTER -> { currentX = remainingSpace / 2f; gap = 0; }
                case END -> { currentX = remainingSpace; gap = 0; }

                case SPACE_BETWEEN -> {
                    currentX = 0;
                    gap = (childCount > 1) ? (float)remainingSpace / (childCount - 1) : 0;
                }
                case SPACE_AROUND -> {
                    gap = (float)remainingSpace / childCount;
                    currentX = gap / 2f;
                }
                case SPACE_EVENLY -> {
                    gap = (float)remainingSpace / (childCount + 1);
                    currentX = gap;
                }
            }
        }

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
        /*
        g.setColor(Color.lightGray);
        g.fillRect(componentBox.x, componentBox.y, componentBox.width, componentBox.height);
        // */
    }
}
