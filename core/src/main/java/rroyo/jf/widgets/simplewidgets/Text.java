package rroyo.jf.widgets.simplewidgets;

import lombok.Getter;
import lombok.Setter;
import rroyo.jf.widgets.basewidgets.Widget;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

public class Text extends Widget {

    private static final FontRenderContext TEXT_FRC =
            new FontRenderContext(
                    null,
                    true,
                    true
            );

    @Getter
    protected String text;

    @Getter
    protected Font font = new Font(
            "Arial",
            Font.PLAIN,
            14
    );

    @Getter
    @Setter
    protected Color color = Color.black;

    @Getter
    @Setter
    protected Color bgColor;

    public Text(String text) {
        this.text = text;
    }

    public Text(String text, Font font) {
        this.text = text;
        this.font = font;
    }

    public Text(String text, Color color) {
        this.text = text;
        this.color = color;
    }

    public Text(String text, Font font, Color color) {
        this.text = text;
        this.font = font;
        this.color = color;
    }

    public Text setText(String text) {
        this.text = text;
        invalidateLayout();
        return this;
    }

    public Text setFont(Font font) {
        this.font = font;
        invalidateLayout();
        return this;
    }

    @Override
    protected void layoutRecalculate() {
        TextMetrics metrics = measureText();
        int width = metrics.width();
        int height = metrics.height();

        if (!isOverflow()) {
            if (father != null && father.getSize().width > 0) {
                width = Math.clamp(father.getSize().width - local.x, 0, width);
            }
            if (father != null && father.getSize().height > 0) {
                height = Math.clamp(father.getSize().height - local.y, 0, height);
            }
        }

        setSize(width, height);
    }

    private TextMetrics measureText() {
        String[] lines = text.split("\n", -1);

        int lineHeight = 0;
        int ascent = 0;

        double minX = 0;
        double maxX = 0;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            Rectangle2D logicalBounds = font.getStringBounds(line, TEXT_FRC);

            GlyphVector glyphVector = font.createGlyphVector(TEXT_FRC, line);
            Rectangle2D visualBounds = glyphVector.getVisualBounds();

            minX = Math.min(minX, visualBounds.getX());

            maxX = Math.max(maxX, Math.max(logicalBounds.getWidth(), visualBounds.getMaxX()));

            if (i == 0) {
                LineMetrics metrics = font.getLineMetrics(line, TEXT_FRC);
                lineHeight = (int) Math.ceil(metrics.getHeight());
                ascent = (int) Math.ceil(metrics.getAscent());
            }
        }

        int width = (int) Math.ceil(maxX - minX);
        int height = lines.length * lineHeight;

        return new TextMetrics(
                width,
                height,
                lineHeight,
                ascent,
                (int) Math.floor(minX),
                lines
        );
    }

    @Override
    protected void design(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        Rectangle bounds = getBounds();

        if (bgColor != null) {
            g2d.setColor(bgColor);

            g2d.fillRect(
                    0,
                    0,
                    bounds.width,
                    bounds.height
            );
        }

        g2d.setFont(font);

        g2d.clipRect(
                0,
                0,
                bounds.width,
                bounds.height
        );

        g2d.setColor(color);

        TextMetrics metrics = measureText();

        int x = -metrics.minX();

        for (int i = 0; i < metrics.lines().length; i++) {
            int y =
                    metrics.ascent()
                            + i * metrics.lineHeight();

            g2d.drawString(
                    metrics.lines()[i],
                    x,
                    y
            );
        }

        g2d.dispose();
    }

    private record TextMetrics(
            int width,
            int height,
            int lineHeight,
            int ascent,
            int minX,
            String[] lines
    ) {
    }
}