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

    public JFRow addChilds(@NotNull JFComponent... children) {
        for (JFComponent child : children) {

            if (child.getClass() == JFCenter.class)
                throw new IllegalArgumentException("Error: Cannot add JFCenter in to a Row");

            super.addChild(child);
        }
        return this;
    }

    @Override
    protected void layoutRecalculate() {

        int totalChildrenWidth = 0;
        int maxChildHeight = 0;

        for (JFComponent child : childList) {
            totalChildrenWidth += child.componentBox.width;
            maxChildHeight = Math.max(maxChildHeight, child.componentBox.height);
        }

        int finalWidth = 0;
        if (parent != null) {
            finalWidth = (parent.componentBox.width > 0)
                    ? parent.componentBox.width
                    : (parent.getClass() == JFCenter.class)
                        ? getComponentFromTree(JFContainer.class, JFSizedBox.class).componentBox.width
                        : (parent.getClass() == JFRow.class)
                            ? totalChildrenWidth
                            : 0//
            ;
        }



    }

    @Override
    protected void design(Graphics g) {

    }
}
