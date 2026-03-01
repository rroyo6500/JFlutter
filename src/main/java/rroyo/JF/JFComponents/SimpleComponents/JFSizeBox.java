package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

public class JFSizeBox extends JFComponent {

    @Override
    public JFSizeBox addChild(@NotNull JFComponent child) {
        this.childList.clear();
        super.addChild(child);
        return this;
    }

    @Override
    public void layoutRecalculate() {

    }

    @Override
    public void design(Graphics g) {

    }

}
