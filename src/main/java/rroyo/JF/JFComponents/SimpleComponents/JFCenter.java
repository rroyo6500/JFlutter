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
        return super.addChild(child);
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
            JFRow row = (JFRow) child;

            if (row.maa == JFRow.mainAxisAlignment.DEFAULT)
                row.mainAxisAlignment(JFRow.mainAxisAlignment.CENTER);
            if (row.caa == JFRow.crossAxisAlignment.DEFAULT)
                row.crossAxisAlignment(JFRow.crossAxisAlignment.CENTER);
        } else if (child.getClass() == JFColumn.class) {
            JFColumn column = (JFColumn) child;

            if (column.maa == JFColumn.mainAxisAlignment.DEFAULT)
                column.mainAxisAlignment(JFColumn.mainAxisAlignment.CENTER);
            if (column.caa == JFColumn.crossAxisAlignment.DEFAULT)
                column.crossAxisAlignment(JFColumn.crossAxisAlignment.CENTER);
        }

    }

    @Override
    protected void design(Graphics g) {

    }
}
