package rroyo.JF.JFComponents;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public abstract class JFComplexComponent extends JFComponent{

    public final JFComponent component;

    public JFComplexComponent(JFComponent component) {
        this.component = component;
    }

    @Override
    public void init(@NotNull JFComponent parent) {
        if (this.component != null) {
            this.component.init(parent);
            int indexComplexComponent = parent.childList.indexOf(this);
            parent.childList.set(indexComplexComponent, this.component);
        }
    }

    @Override
    public void design(Graphics g) {

    }

    @Override
    public void layoutRecalculate() {

    }

}
