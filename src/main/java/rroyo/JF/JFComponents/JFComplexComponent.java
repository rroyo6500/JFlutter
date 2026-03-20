package rroyo.JF.JFComponents;

import java.awt.*;

public abstract class JFComplexComponent extends JFComponent{



    @Override
    protected abstract void layoutRecalculate();

    @Override
    protected abstract void design(Graphics g);

}
