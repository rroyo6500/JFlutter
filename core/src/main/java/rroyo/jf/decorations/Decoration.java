package rroyo.jf.decorations;

import lombok.Getter;
import lombok.Setter;
import rroyo.jf.widgets.basewidgets.Widget;

import java.awt.*;

@Getter
public class Decoration {

    private Widget fatherWidget;

    @Setter
    private Color color;

    @Setter
    private Border border;

    @Setter
    private BoxShadow shadow;

    private int borderRadius;

    public Decoration(Color color, Widget fatherWidget) {
        this.color = color;
        this.fatherWidget = fatherWidget;
    }

    public Decoration(Color color) {
        this.color = color;
    }

    public Decoration setBorderRadius(int borderRadius) {
        this.borderRadius = Math.max(0, borderRadius);
        return this;
    }

    public Decoration setRadius(int borderRadius) {
        return setBorderRadius(borderRadius);
    }

    public void draw(Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        Shape clip = g2d.getClip();
        g2d.setClip(null);

        int clampedRadius = Math.clamp(Math.min(width, height) / 2, 0, borderRadius);
        int arc = clampedRadius * 2;

        if (shadow != null)
            shadow.drawShadow(g, x, y, width, height, clampedRadius);

        g.setColor(color);
        if (arc > 0) {
            g.fillRoundRect(x, y, width, height, arc, arc);
        } else {
            g.fillRect(x, y, width, height);
        }

        if (border != null)
            border.drawBorder(g, x, y, width, height, clampedRadius);

        g2d.setClip(clip);
    }

}
