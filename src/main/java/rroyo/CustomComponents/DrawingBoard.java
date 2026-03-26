package rroyo.CustomComponents;

import rroyo.JF.Enums.ActionEventTypes;
import rroyo.JF.Enums.MouseButtons;
import rroyo.JF.JFComponents.JFComponent;
import rroyo.JF.JFEvents.JFInteractiveEventSource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Interactive board component that lets the user paint a pixel grid.
 *
 * @author rroyo
 */
public class DrawingBoard extends JFComponent implements JFInteractiveEventSource {

    /**
     * Matrix storing the color assigned to each drawable cell.
     */
    private final List<List<Color>> grid = new ArrayList<>();
    /**
     * Pixel size used to render each logical cell.
     */
    private int scale = 1;

    /**
     * Initial color used to fill every cell on creation.
     */
    private final Color defaultColor;
    /**
     * Active brush color used while painting.
     */
    private Color currentColor;

    /**
     * Flag indicating whether pointer movement should paint cells.
     */
    private boolean isDrawing = false;

    /**
     * Creates a drawing board with white as default background color.
     *
     * @param scale pixel size of each board cell
     * @param currentColor initial brush color
     */
    public DrawingBoard(int scale, Color currentColor) {
        this(scale, currentColor, Color.WHITE);
    }

    /**
     * Creates a drawing board with custom brush and background colors.
     *
     * @param scale pixel size of each board cell
     * @param currentColor initial brush color
     * @param defaultColor background color used to initialize the grid
     */
    public DrawingBoard(int scale, Color currentColor, Color defaultColor) {
        super();
        this.scale = scale;
        this.currentColor = currentColor;
        this.defaultColor = defaultColor;
        this.addActionListener(e -> {
            if (e.getAction() == ActionEventTypes.DOWN && e.getAction().getButton() == MouseButtons.LEFT)
                isDrawing = true;
            else if (e.getAction() == ActionEventTypes.UP && e.getAction().getButton() == MouseButtons.RIGHT)
                isDrawing = false;
        });
        this.addHoverListener(e -> {
            if (isDrawing) {
                int x = e.getMouseX()/scale;
                int y = e.getMouseY()/scale;

                grid.get(y).set(x, currentColor);
            }
        });
    }

    /**
     * Returns the brush color currently used for drawing.
     *
     * @return current brush color
     */
    public Color getCurrentColor() {
        return currentColor;
    }

    /**
     * Updates the current brush color.
     *
     * @param currentColor new brush color to apply
     */
    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    @Override
    protected void layoutRecalculate() {
        setSize(parent.getWidth(), parent.getHeight());
        for (int i = 0; i < getHeight()/scale; i++) {
            grid.add(new ArrayList<>());
        }
        for (List<Color> row : grid) {
            for (int i = 0; i < getWidth()/scale; i++) {
                row.add(defaultColor);
            }
        }
    }

    @Override
    protected void design(Graphics g) {

        int x = 0;
        int y = 0;
        for (List<Color> row : grid) {
            for (Color color : row) {
                g.setColor(color);
                g.fillRect(x, y, scale, scale);
                x+=scale;
            }
            x = 0;
            y+=scale;
        }

    }



}
