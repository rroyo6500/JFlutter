package rroyo.JF.JFComponents;

import java.awt.*;

public abstract class JFComplexComponent extends JFComponent{

    public final JFComponent component;

    public JFComplexComponent(JFComponent component) {
        this.component = component;
    }

    @Override
    public void layoutRecalculate() {
        component.layout();
    }

    @Override
    public void design(Graphics g) {
        component.draw(g);
    };
}
