package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

public class JFRow extends JFComponent {

    public enum MainAxisAlignment {
        START,
        CENTER,
        END,
        SPACE_BETWEEN,
        SPACE_AROUND,
        SPACE_EVENLY
    }
    public enum CrossAxisAlignment {
        START,
        CENTER,
        END
    }

    public MainAxisAlignment maa = MainAxisAlignment.START;
    public CrossAxisAlignment caa = CrossAxisAlignment.START;

    public JFRow() {
        super();
    }

    public JFRow mainAxisAlingment(MainAxisAlignment maa) {
        this.maa = maa;
        return this;
    }
    public JFRow crossAxisAlingment(CrossAxisAlignment caa) {
        this.caa = caa;
        return this;
    }

    @Override
    public JFRow setSize(int width, int height) {
        super.setSize(width, height);
        return this;
    }

    @Override
    public JFRow setWidth(int width) {
        super.setWidth(width);
        return this;
    }

    @Override
    public JFRow setHeight(int height) {
        super.setHeight(height);
        return this;
    }

    protected void setSizeInternal(int w, int h) {
        componentBox.setSize(w, h);
    }

    @Override
    public JFRow addChild(@NotNull JFComponent child) {
        super.addChild(child);
        return this;
    }

    public JFRow addChilds(@NotNull JFComponent... children) {
        for (JFComponent child : children) {
            addChild(child);
        }
        return this;
    }

    @Override
    public void layoutRecalculate() {
        int totalChildrenWidth = 0;
        int maxChildHeight = 0;

        for (JFComponent child : childList) {
            totalChildrenWidth += child.componentBox.width;
            maxChildHeight = Math.max(maxChildHeight, child.componentBox.height);
        }

        int finalWidth = (parent != null && parent.componentBox.width > 0)
                ? parent.componentBox.width : totalChildrenWidth;

        setSizeInternal(finalWidth, maxChildHeight);

        int remainingSpace = componentBox.width - totalChildrenWidth;
        int childCount = childList.size();

        float currentX = 0;
        float gap = 0;

        if (childCount > 0) {
            switch (maa) {
                case START -> { currentX = 0; gap = 0; }
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

            child.setLocalPositionInternal((int)currentX, childY);
            currentX += child.componentBox.width + gap;
        }
    }

    @Override
    public void design(Graphics g) {

    }
}
