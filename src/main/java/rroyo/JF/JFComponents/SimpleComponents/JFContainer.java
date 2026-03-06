package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

public class JFContainer extends JFComponent {

    private Color color;

    public JFContainer(int width, int height, @Nullable Color color) {
        setSize(width, height);
        this.color = color;
    }

    public JFContainer(int width, int height) {
        this(width, height, null);
    }

    public JFContainer setColor(Color color) {
        this.color = color;
        return this;
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
        if (color == null) {
            System.err.printf(
                    "Warning: Component [JFContainer(%d, %d, null)] can be replaced by 'JFSizedBox'%n",
                    componentBox.width, componentBox.height
            );
            return;
        }

        g.setColor(color);
        g.fillRect(componentBox.x, componentBox.y, componentBox.width, componentBox.height);
    }
}
