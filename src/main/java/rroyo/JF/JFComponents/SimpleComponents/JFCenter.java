package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

public class JFCenter extends JFComponent {

    public JFCenter(JFComponent child) {
        super(true);
        addChild(child);
    }

    @Override
    public JFComponent addChild(@NotNull JFComponent child) {
        childList.clear();
        super.addChild(child);
        return this;
    }

    @Override
    protected void layoutRecalculate() {

        setSize(parent.componentBox.width, parent.componentBox.height);

        if (childList.isEmpty()) return;

        JFComponent child = childList.getFirst();

        int childX = (componentBox.width / 2) - (child.componentBox.width / 2);
        int childY = (componentBox.height / 2) - (child.componentBox.height / 2);

        child.setPosition(childX, childY);

        if (child.getClass() == JFRow.class) {
            ((JFRow) child).mainAxisAlignment(JFRow.mainAxisAlignment.CENTER);
            ((JFRow) child).crossAxisAlignment(JFRow.crossAxisAlignment.CENTER);
        } else if (child.getClass() == JFColumn.class) {
            ((JFColumn) child).mainAxisAlignment(JFColumn.mainAxisAlignment.CENTER);
            ((JFColumn) child).crossAxisAlignment(JFColumn.crossAxisAlignment.CENTER);
        }

    }

    @Override
    protected void design(Graphics g) {

    }
}
