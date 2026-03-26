package rroyo.JF.JFComponents.SimpleComponents;

import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

public abstract class JFCanvas extends JFComponent {

    @Override
    protected void layoutRecalculate() {

    }

    @Override
    protected abstract void design(Graphics g);
}
