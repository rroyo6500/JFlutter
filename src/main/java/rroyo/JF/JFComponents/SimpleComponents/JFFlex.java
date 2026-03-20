package rroyo.JF.JFComponents.SimpleComponents;

import rroyo.JF.Enums.CrossAxisAlignment;
import rroyo.JF.Enums.MainAxisAlignment;
import rroyo.JF.JFComponents.JFComponent;

/**
 * Represents an abstract base class for flexible layout components,
 * allowing customizable main axis and cross axis alignments.
 * JFFlex is designed to be extended to create specific flexbox-style layouts like rows or columns.
 * This class handles the positioning and spacing of child components based on the provided
 * alignment configurations.
 */
public abstract class JFFlex extends JFComponent {

    /**
     * Defines the main axis alignment for the layout in the enclosing `JFFlex` class.
     * The `mainAxisAlignment` property determines how child components are arranged
     * along the main axis of the container (e.g., horizontally in a row or vertically in a column).
     * <br>
     * Default value is `MainAxisAlignment.DEFAULT`, which relies on the default alignment
     * behavior of the layout.
     * <br>
     * Possible values: <br>
     * - `DEFAULT`: Default alignment as defined by the layout. <br>
     * - `START`: Aligns children to the start of the main axis. <br>
     * - `CENTER`: Aligns children at the center of the main axis. <br>
     * - `END`: Aligns children to the end of the main axis. <br>
     * - `SPACE_BETWEEN`: Distributes children with space between them. <br>
     * - `SPACE_AROUND`: Distributes children with equal space around them. <br>
     * - `SPACE_EVENLY`: Distributes children with equal space between and around them. <br>
     */
    public MainAxisAlignment maa = MainAxisAlignment.DEFAULT;

    /**
     * Represents the cross-axis alignment for a layout. The cross-axis determines
     * how components are vertically aligned within a container when the primary layout
     * direction is horizontal, or horizontally aligned when the primary layout direction is vertical.
     * <br>
     * The alignment is specified as an instance of the {@code CrossAxisAlignment} enum,
     * which provides the following options: <br>
     * - {@code DEFAULT}: Uses the default alignment behavior of the container. <br>
     * - {@code START}: Aligns children at the start of the cross-axis. <br>
     * - {@code CENTER}: Centers children along the cross-axis. <br>
     * - {@code END}: Aligns children at the end of the cross-axis. <br>
     * <br>
     * The default value is {@code CrossAxisAlignment.DEFAULT}.
     */
    public CrossAxisAlignment caa = CrossAxisAlignment.DEFAULT;

    /**
     * Constructs a new instance of the JFFlex class. This constructor initializes the object
     * as a flexible layout container, enabling the management of child components' alignment
     * and spacing based on the flexbox-inspired system.
     * <br>
     * The superclass constructor is called with a parameter set to true, indicating that this
     * layout component requires at least one child component for its layout computation.
     */
    public JFFlex() {
        super(true);
    }

    /**
     * Sets the main axis alignment for the layout. The main axis alignment determines how
     * children are aligned along the main axis of the container.
     *
     * @param maa The desired main axis alignment, represented as an instance of MainAxisAlignment.
     *            This can be one of the following values:
     *            DEFAULT, START, CENTER, END, SPACE_BETWEEN, SPACE_AROUND, SPACE_EVENLY.
     * @return The current JFFlex instance, allowing method chaining.
     */
    public JFFlex mainAxisAlignment(MainAxisAlignment maa) {
        this.maa = maa;
        return this;
    }

    /**
     * Sets the cross axis alignment for the layout. The cross axis alignment determines how
     * children are aligned along the cross axis of the container.
     *
     * @param caa The desired cross axis alignment, represented as an instance of CrossAxisAlignment.
     *            This can be one of the following values: DEFAULT, START, CENTER, END.
     * @return The current JFFlex instance, allowing method chaining.
     */
    public JFFlex crossAxisAlignment(CrossAxisAlignment caa) {
        this.caa = caa;
        return this;
    }

    /**
     * Calculates the starting position and gap between child components based on the remaining space
     * and the number of children, considering the main axis alignment setting.
     *
     * @param remainingSpace The amount of space remaining after accounting for the total size of child components.
     * @param childCount The number of child components to be positioned within the layout.
     * @return A float array containing two values: <br>
     *         - The initial starting position (index 0) for positioning the children. <br>
     *         - The gap (index 1) to be applied between each child component.
     */
    protected float[] calculateFlexChildPositions(int remainingSpace, int childCount) {
        float currentPos = 0;
        float gap = 0;

        if (childCount > 0) {
            switch (maa) {
                case START, DEFAULT -> { currentPos = 0; gap = 0; }
                case CENTER -> { currentPos = remainingSpace / 2f; gap = 0; }
                case END -> { currentPos = remainingSpace; gap = 0; }

                case SPACE_BETWEEN -> {
                    currentPos = 0;
                    gap = (childCount > 1) ? (float)remainingSpace / (childCount - 1) : 0;
                }
                case SPACE_AROUND -> {
                    gap = (float)remainingSpace / childCount;
                    currentPos = gap / 2f;
                }
                case SPACE_EVENLY -> {
                    gap = (float)remainingSpace / (childCount + 1);
                    currentPos = gap;
                }
            }
        }

        return new float[]{currentPos, gap};
    }

}
