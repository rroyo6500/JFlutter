package rroyo.jf.interfaces;

import java.awt.*;

@FunctionalInterface
public interface Painter {
    void paint(Graphics g, Dimension dimension);
}
