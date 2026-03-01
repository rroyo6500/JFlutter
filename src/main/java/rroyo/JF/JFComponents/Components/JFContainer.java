package rroyo.JF.JFComponents.Components;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

public class JFContainer extends JFComponent {

    public Color color;

    public JFContainer() {
        super();
    }

    public JFContainer setColor(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public JFContainer setSize(int width, int height) {
        super.setSize(width, height);
        return this;
    }

    @Override
    public JFContainer setWidth(int width) {
        super.setWidth(width);
        return this;
    }

    @Override
    public JFContainer setHeight(int height) {
        super.setHeight(height);
        return this;
    }

    @Override
    public JFContainer addChild(@NotNull JFComponent child) {
        this.childList.clear();
        super.addChild(child);
        return this;
    }

    @Override
    public void layoutRecalculate() {

    }

    @Override
    public void design(Graphics g) {
        if (color == null) return;

        g.setColor(color);
        g.fillRect(componentBox.x, componentBox.y, componentBox.width, componentBox.height);

    }
}
