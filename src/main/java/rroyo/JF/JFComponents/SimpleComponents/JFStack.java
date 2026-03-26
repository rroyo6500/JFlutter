package rroyo.JF.JFComponents.SimpleComponents;

import rroyo.JF.Enums.Alignment;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

public class JFStack extends JFComponent {

    private Alignment alignment;

    public JFStack(Alignment alignment) {
        super(true);
        this.alignment = alignment;
    }

    @Override
    protected void layoutRecalculate() {
        setSize(parent.getWidth(), parent.getHeight());
    }

    @Override
    protected void design(Graphics g) {



    }
}
