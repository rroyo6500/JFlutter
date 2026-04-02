package rroyo.CustomComponents;

import rroyo.JF.Enums.ActionEventTypes;
import rroyo.JF.Enums.KeyEventTypes;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;
import rroyo.JF.JFEvents.JFInteractiveComponent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Interactive pixel board that allows painting a grid of color cells with the mouse.
 * <p>
 * The component models a drawable surface as a matrix of logical cells. Each cell stores a
 * color and is rendered as a square of {@code scale x scale} pixels. Mouse interaction is used
 * to toggle drawing mode and to apply the current brush color while the pointer moves across
 * the board.
 *
 * @author rroyo
 */
public class DrawingBoard extends JFComponent implements JFInteractiveComponent {

    /**
     * Matrix storing the current color of every logical cell on the board.
     * <p>
     * The outer list represents rows and the inner lists represent the cells of each row.
     */
    private final List<List<Color>> grid = new ArrayList<>();
    /**
     * Size in pixels of each logical cell rendered by the board.
     */
    private final int scale;

    /**
     * Color used to initialize every cell whenever the board layout is built.
     */
    private final Color defaultColor;
    /**
     * Color currently used as the active drawing brush.
     */
    private Color currentColor;

    /**
     * Indicates whether hover movement should currently paint cells.
     * <p>
     * The board toggles this state when it receives an {@code UP} action event.
     */
    private boolean isDrawing = false;

    /**
     * Creates a drawing board with a white background grid.
     *
     * @param scale pixel size of each logical cell
     * @param currentColor initial brush color
     */
    public DrawingBoard(int scale, Color currentColor) {
        this(scale, currentColor, Color.WHITE);
    }

    /**
     * Creates a drawing board with custom brush and default cell colors.
     * <p>
     * The constructor also wires the interaction behavior:
     * pressing and releasing the mouse toggles drawing mode through the action listener,
     * and hover movement paints the cell under the pointer while drawing mode is active.
     *
     * @param scale pixel size of each logical cell
     * @param currentColor initial brush color
     * @param defaultColor color used to initialize all cells
     */
    public DrawingBoard(int scale, Color currentColor, Color defaultColor) {
        super();
        this.scale = scale;
        this.currentColor = currentColor;
        this.defaultColor = defaultColor;
        this.addActionListener(e -> {
            if (e.getAction() == ActionEventTypes.UP)
                isDrawing = !isDrawing;
        });
        this.addHoverListener(e -> {
            if (isDrawing) {
                int x = (e.getMouseX() - getComponentBox().x) / scale;
                int y = (e.getMouseY() - getComponentBox().y) / scale;

                try {
                    grid.get(y).set(x, this.currentColor);
                } catch (IndexOutOfBoundsException ignored) {}
            }
        });
        this.addKeyListener(e -> {
            if (e.getType() == KeyEventTypes.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_R){
                for (List<Color> row : grid) {
                    row.replaceAll(ignored -> defaultColor);
                }
            }

        });
    }

    /**
     * Returns the currently active brush color.
     *
     * @return active drawing color
     */
    public Color getCurrentColor() {
        return currentColor;
    }

    /**
     * Updates the brush color that will be applied to future painted cells.
     *
     * @param currentColor new active drawing color
     */
    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    /**
     * Returns the color used when the grid is initialized.
     *
     * @return default cell color
     */
    public Color getDefaultColor() {
        return defaultColor;
    }

    /**
     * Rebuilds the grid so it matches the current size inherited from the parent container.
     * <p>
     * The board stretches to the full size of its parent. Based on that size and the configured
     * cell scale, it creates a new matrix of rows and columns filled with the default color.
     */
    @Override
    protected void layoutRecalculate() {
        setSize(parent.getWidth(), parent.getHeight());
        grid.clear();

        for (int i = 0; i < getHeight() / scale; i++) {
            grid.add(new ArrayList<>());
        }
        for (List<Color> row : grid) {
            for (int i = 0; i < getWidth() / scale; i++) {
                row.add(defaultColor);
            }
        }
    }

    /**
     * Paints the board by iterating over the logical grid and drawing one filled square per cell.
     * <p>
     * Rendering starts from the component's absolute top-left corner and advances in cell-size
     * increments across each row and then down through the grid.
     *
     * @param g graphics context used to render the board
     */
    @Override
    protected void design(Graphics g) {

        int x = componentBox.x;
        int y = componentBox.y;
        for (List<Color> row : grid) {
            for (Color color : row) {
                g.setColor(color);
                g.fillRect(x, y, scale, scale);
                x += scale;
            }
            x = componentBox.x;
            y += scale;
        }

    }

}
