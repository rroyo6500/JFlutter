package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.Enums.CrossAxisAlignment;
import rroyo.JF.Enums.MainAxisAlignment;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;
import rroyo.JF.JFComponents.BaseComponent.JFMultiChildComponent;

/**
 * Abstract base class for row- and column-style flex layouts.
 * <p>
 * {@code JFFlex} centralizes the configuration and helper logic shared by the concrete
 * horizontal and vertical flex containers. It stores the main-axis and cross-axis alignment
 * settings and provides reusable calculations for child distribution along the main axis.
 *
 * @author rroyo
 */
public abstract class JFFlex extends JFComponent implements JFMultiChildComponent<JFFlex> {

    /**
     * Alignment strategy used on the main axis of the layout.
     * <p>
     * For rows the main axis is horizontal, and for columns it is vertical.
     */
    protected MainAxisAlignment maa = MainAxisAlignment.DEFAULT;

    /**
     * Alignment strategy used on the cross axis of the layout.
     * <p>
     * For rows the cross axis is vertical, and for columns it is horizontal.
     */
    protected CrossAxisAlignment caa = CrossAxisAlignment.DEFAULT;

    /**
     * Creates a flex container whose layout depends on child sizes.
     */
    public JFFlex() {
        super(true);
    }

    /**
     * Sets how free space is handled on the main axis.
     *
     * @param maa main-axis alignment strategy
     * @return current flex container for fluent configuration
     */
    public JFFlex mainAxisAlignment(MainAxisAlignment maa) {
        this.maa = maa;
        return this;
    }

    /**
     * Sets how children are aligned on the cross axis.
     *
     * @param caa cross-axis alignment strategy
     * @return current flex container for fluent configuration
     */
    public JFFlex crossAxisAlignment(CrossAxisAlignment caa) {
        this.caa = caa;
        return this;
    }

    /**
     * Adds a single child while enforcing the structural rules of flex containers.
     *
     * @param child child to attach
     * @return current flex container
     * @throws IllegalArgumentException when the child is a {@link JFCenter}
     */
    @Override
    public JFFlex addChild(@NotNull JFComponent child) {
        if (child.getClass() == JFCenter.class)
            throw new IllegalArgumentException("Error: Cannot add JFCenter in to a " + this.getClass().getSimpleName());

        attachChild(child);
        return this;
    }

    /**
     * Adds multiple children while enforcing the structural rules of flex containers.
     * <p>
     * {@link JFCenter} is intentionally forbidden as a direct child because centering logic would
     * conflict with the explicit row or column positioning performed by flex layouts.
     *
     * @param children children to attach in order
     * @return current flex container
     * @throws IllegalArgumentException when any child is a {@link JFCenter}
     */
    @Override
    public JFFlex addChilds(@NotNull JFComponent... children) {
        for (JFComponent child : children) {
            addChild(child);
        }
        return this;
    }

    /**
     * Calculates the initial offset and inter-child gap for the chosen main-axis alignment.
     * <p>
     * The first returned value indicates where the first child should be placed on the main axis.
     * The second value indicates the spacing that should be inserted after each child. Concrete row
     * and column implementations use these values to place children on X or Y depending on their
     * orientation.
     *
     * @param remainingSpace free space left after subtracting the total size of all children
     * @param childCount number of children to distribute
     * @return array containing {@code startOffset} at index 0 and {@code gap} at index 1
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
                    gap = (childCount > 1) ? (float) remainingSpace / (childCount - 1) : 0;
                }
                case SPACE_AROUND -> {
                    gap = (float) remainingSpace / childCount;
                    currentPos = gap / 2f;
                }
                case SPACE_EVENLY -> {
                    gap = (float) remainingSpace / (childCount + 1);
                    currentPos = gap;
                }
            }
        }

        return new float[]{currentPos, gap};
    }

}
