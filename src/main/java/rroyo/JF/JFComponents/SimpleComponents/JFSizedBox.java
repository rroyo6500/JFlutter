package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

public class JFSizedBox extends JFComponent {

    public JFSizedBox(int width, int height) {
        setSize(width, height);
    }

    @Override
    public JFComponent addChild(@NotNull JFComponent child) {
        childList.clear();
        super.addChild(child);
        return this;
    }

    @Override
    protected void layoutRecalculate() {

    }

    @Override
    protected void design(Graphics g) {

    }
}
