package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

public class JFCenter extends JFComponent {

    public JFCenter() {
        super();
    }

    public JFCenter(@NotNull JFComponent child) {
        super();
        addChild(child);
    }

    @Override
    public JFCenter addChild(@NotNull JFComponent child) {
        this.childList.clear();
        super.addChild(child);
        return this;
    }

    @Override
    public void layoutRecalculate() {

        setSize(parent.componentBox.width, parent.componentBox.height);

        if (childList.isEmpty()) {
            return;
        }

        JFComponent child = childList.getFirst();

        int childX = (componentBox.width / 2) - (child.componentBox.width / 2);
        int childY = (componentBox.height / 2) - (child.componentBox.height / 2);

        child.setLocalPositionInternal(childX, childY);
    }

    @Override
    public void design(Graphics g) {

    }

    @Override
    public JFCenter setSize(int width, int height) {
        super.setSize(width, height);
        return this;
    }
}
