package rroyo.JF.Decorations;

import java.awt.*;

/**
 * Immutable shadow style used to render offset drop shadows.
 *
 * @param shadowColor shadow base color
 * @param offsetX horizontal shadow offset
 * @param offsetY vertical shadow offset
 * @param blurRadius blur strength in pixels
 * @author rroyo
 */
public record BoxShadow(Color shadowColor, int offsetX, int offsetY, int blurRadius) {

    /**
     * Creates a shadow without blur.
     *
     * @param shadowColor shadow base color
     * @param offsetX horizontal shadow offset
     * @param offsetY vertical shadow offset
     */
    public BoxShadow(Color shadowColor, int offsetX, int offsetY) {
        this(shadowColor, offsetX, offsetY, 0);
    }

    /**
     * Draws the shadow with square corners.
     *
     * @param g graphics context used for rendering
     * @param x top-left x-coordinate of the target box
     * @param y top-left y-coordinate of the target box
     * @param width target box width
     * @param height target box height
     */
    public void drawShadow(Graphics g, int x, int y, int width, int height) {
        drawShadow(g, x, y, width, height, 0);
    }

    /**
     * Draws the shadow with an optional corner radius and blur.
     *
     * @param g graphics context used for rendering
     * @param x top-left x-coordinate of the target box
     * @param y top-left y-coordinate of the target box
     * @param width target box width
     * @param height target box height
     * @param borderRadius corner radius used to shape the shadow
     */
    public void drawShadow(Graphics g, int x, int y, int width, int height, int borderRadius) {
        int clampedRadius = Math.max(0, borderRadius);
        boolean useRoundedCorners = clampedRadius > 0;

        if (!(g instanceof Graphics2D graphics2D)) {
            g.setColor(shadowColor);
            drawShape(g, x + offsetX, y + offsetY, width, height, clampedRadius);
            return;
        }

        Graphics2D shadowGraphics = (Graphics2D) graphics2D.create();
        try {
            int requestedBlur = Math.max(0, blurRadius);
            int maxAllowedBlur = Math.max(0, (Math.min(width, height) - 1) / 2);
            int clampedBlur = Math.min(requestedBlur, maxAllowedBlur);

            if (clampedBlur <= 0) {
                shadowGraphics.setColor(shadowColor);
                drawShape(shadowGraphics, x + offsetX, y + offsetY, width, height, clampedRadius);
                return;
            }

            int baseX = x + offsetX + clampedBlur;
            int baseY = y + offsetY + clampedBlur;
            int baseWidth = width - (clampedBlur * 2);
            int baseHeight = height - (clampedBlur * 2);

            int baseAlpha = shadowColor.getAlpha();
            int perLayerAlpha = Math.max(1, Math.round(baseAlpha / (float) (clampedBlur + 1)));
            int coreRadius = useRoundedCorners ? Math.max(0, clampedRadius - clampedBlur) : 0;

            for (int layer = clampedBlur; layer >= 1; layer--) {
                Color layerColor = new Color(
                        shadowColor.getRed(),
                        shadowColor.getGreen(),
                        shadowColor.getBlue(),
                        perLayerAlpha
                );
                shadowGraphics.setColor(layerColor);
                drawShape(
                        shadowGraphics,
                        baseX - layer,
                        baseY - layer,
                        baseWidth + (layer * 2),
                        baseHeight + (layer * 2),
                        useRoundedCorners ? (coreRadius + layer) : 0
                );
            }

            Color coreColor = new Color(
                    shadowColor.getRed(),
                    shadowColor.getGreen(),
                    shadowColor.getBlue(),
                    Math.max(1, Math.round(baseAlpha * 0.7f))
            );
            shadowGraphics.setColor(coreColor);
            drawShape(shadowGraphics, baseX, baseY, baseWidth, baseHeight, coreRadius);
        } finally {
            shadowGraphics.dispose();
        }
    }

    /**
     * Draws a filled rectangle or rounded rectangle according to the requested radius.
     *
     * @param g graphics context used for rendering
     * @param x top-left x-coordinate
     * @param y top-left y-coordinate
     * @param width shape width
     * @param height shape height
     * @param radius corner radius in pixels
     */
    private void drawShape(Graphics g, int x, int y, int width, int height, int radius) {
        if (radius > 0) {
            int arc = radius * 2;
            g.fillRoundRect(x, y, width, height, arc, arc);
        } else {
            g.fillRect(x, y, width, height);
        }
    }

}
