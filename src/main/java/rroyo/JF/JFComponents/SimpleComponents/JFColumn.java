package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

public class JFColumn extends JFComponent {

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

    public JFColumn() {
        super();
    }

    public JFColumn mainAxisAlingment(MainAxisAlignment maa) {
        this.maa = maa;
        return this;
    }
    public JFColumn crossAxisAlingment(CrossAxisAlignment caa) {
        this.caa = caa;
        return this;
    }

    @Override
    public JFColumn setSize(int width, int height) {
        super.setSize(width, height);
        return this;
    }

    @Override
    public JFColumn setWidth(int width) {
        super.setWidth(width);
        return this;
    }

    @Override
    public JFColumn setHeight(int height) {
        super.setHeight(height);
        return this;
    }

    protected void setSizeInternal(int w, int h) {
        componentBox.setSize(w, h);
    }

    @Override
    public JFColumn addChild(@NotNull JFComponent child) {
        super.addChild(child);
        return this;
    }

    public JFColumn addChilds(@NotNull JFComponent... children) {
        for (JFComponent child : children) {
            if (child.getClass() == JFCenter.class)
                throw new RuntimeException("JFCenter cannot be added directly to a JFColumn");
            addChild(child);
        }
        return this;
    }

    @Override
    public void layoutRecalculate() {
        int totalChildrenHeight = 0;
        int maxChildWidth = 0;

        for (JFComponent child : childList) {
            totalChildrenHeight += child.componentBox.height;
            maxChildWidth = Math.max(maxChildWidth, child.componentBox.width);
        }

        int finalHeight = (parent != null && parent.componentBox.height > 0)
                ? parent.componentBox.height : componentBox.height > 0 ? componentBox.height : totalChildrenHeight;

        setSizeInternal(maxChildWidth, finalHeight);

        int remainingSpace = componentBox.height - totalChildrenHeight;
        int childCount = childList.size();

        float currentY = 0;
        float gap = 0;

        if (childCount > 0) {
            switch (maa) {
                case START -> { currentY = 0; gap = 0; }
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

            child.setLocalPositionInternal(childX, (int) currentY);
            currentY += child.componentBox.height + gap;
        }
    }

    @Override
    public void design(Graphics g) {

    }

}
