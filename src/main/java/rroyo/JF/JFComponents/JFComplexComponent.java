package rroyo.JF.JFComponents;

import java.awt.*;

public abstract class JFComplexComponent extends JFComponent{

    public final JFComponent content;

    public JFComplexComponent(JFComponent content) {
        super(true);
        this.content = content;
        addChild(content);
    }

    @Override
    protected void layoutRecalculate() {
        setSize(content.componentBox.width, content.componentBox.height);
        content.setPosition(0, 0);
    }

    @Override
    protected void design(Graphics g) {
    }

}
