package rroyo.JF.JFComponents;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an abstract component in a graphical framework.
 * This class provides the foundational structure for creating custom graphical components
 * and managing their relationships within a component hierarchy.
 */
public abstract class JFComponent {

    /**
     * Represents the parent component in the hierarchy of graphical components.
     *
     * <ul>
     * <li>Holds a reference to the immediate parent of this {@code JFComponent}
     * in the component tree.</li>
     * <li>Allows child components to access contextual information and resources
     * provided by their parent, such as the {@code Graphics} context.</li>
     * </ul>
     *
     * This variable helps establish the structural hierarchy of {@code JFComponent}
     * objects in the graphical framework.
     */
    public JFComponent parent;

    /**
     * An array containing the child components of this {@code JFComponent}.
     * <p>
     * This variable defines the hierarchical relationship between components,
     * where each {@code JFComponent} can serve as a parent to one or more child components.
     * <p>
     * Key characteristics of {@code children}:
     * - Maintains references to the immediate child components in the hierarchy.
     * - Enables nested rendering and event delegation by organizing components within a tree structure.
     * - May be used to iterate and manage grouped behaviors, such as layout adjustments or graphical updates.
     */
    public final List<JFComponent> childList = new ArrayList<>();

    /**
     * Represents the geometric boundary of the component.
     * <p>
     * The {@code componentBox} defines the shape and area within which the
     * component is visually rendered and responds to graphical operations.
     * <p>
     * Key characteristics:
     * - It is defined as a {@link Polygon}, allowing for complex, non-rectangular shapes.
     * - Used primarily for collision detection, layout management, or other
     * graphical computations involving the component's boundaries.
     * - Typically aligned with the visual representation of the component.
     * <p>
     * This variable is expected to be initialized and updated as necessary to
     * accurately represent the component's current geometric bounds.
     */
    public Rectangle componentBox = new Rectangle(0, 0, 0, 0);

    private boolean layoutDirty = true;

    /**
     * Represents the horizontal local position of this component relative to its parent.
     * The value is used in conjunction with {@code localY} to determine the component’s
     * absolute position within the parent’s coordinate system.
     * <p>
     * The local position is set using methods such as {@code setPosition}, {@code setX},
     * or indirectly through other interactions like adding the component to a parent.
     * <p>
     * Constraints:
     * - Must remain within the bounds of the parent component if a parent is assigned.
     * - The absolute position is recalculated whenever this value is updated.
     * <p>
     * Used by:
     * - {@code setPosition(int x, int y)} to set the local coordinates.
     * - {@code updateAbsolutePositionFromLocal()} for computing the absolute position.
     * - {@code validateWithinParent()} to ensure the position adheres to the parent’s boundaries.
     * <p>
     * Default value is {@code 0}, indicating the leftmost edge of the parent.
     */
    protected int localX = 0;

    /**
     * Represents the local Y-coordinate of this component relative to its parent.
     * This value, along with the local X-coordinate, determines the component's
     * position within the parent component's bounds.
     * <p>
     * The local Y-coordinate is used in conjunction with the absolute position
     * calculation to ensure proper alignment within the parent. The absolute Y-coordinate
     * of the component is derived by adding this local value to the parent's absolute
     * Y-coordinate.
     * <p>
     * Constraints ensure that the local Y-coordinate does not position the component
     * outside the parent's boundaries. If violated, appropriate validation methods will throw
     * exceptions to prevent inconsistencies in the component hierarchy.
     * <p>
     * Modifying this value directly should typically be avoided; instead, use the
     * {@code setPosition} or {@code setY} methods to update this coordinate to ensure proper
     * validation and corresponding updates to the component's absolute position.
     */
    protected int localY = 0;

    /**
     * Initializes a new instance of the JFComponent class.
     * <p>
     * This constructor sets up the default properties of the component, including
     * initializing its local bounding box to zero dimensions at the origin point
     * (0, 0). The `componentBox` is used to define the position and size of the
     * component in screen or parent-relative coordinates.
     */
    public JFComponent() {}

    /**
     * Initializes the component with the specified parent component.
     * This method sets the parent of the current component, adjusts
     * its absolute position based on its local coordinates, and validates
     * that the component is appropriately positioned and sized within the
     * parent's bounds.
     *
     * @param parent the parent component to which this component will belong. Must not be null.
     * @throws IllegalStateException if the component's size or position exceeds the parent's bounds.
     */
    public void init(@NotNull JFComponent parent) {
        this.parent = parent;
        invalidateLayout();
    }

    /**
     * Updates the absolute position of this component based on its local position
     * relative to its parent component. This method recalculates the coordinates
     * of the component's bounding box (`componentBox`) by adding the local
     * position offsets (`localX` and `localY`) to the absolute coordinates
     * of the parent component's bounding box.
     * <p>
     * If the component does not have a parent (`parent == null`), this method
     * performs no action.
     */
    private void updateAbsolutePositionFromLocal() {
        if (parent != null)
            componentBox.setLocation(parent.componentBox.x + localX, parent.componentBox.y + localY);

        for (JFComponent child : childList) {
            child.updateAbsolutePositionFromLocal();
        }
    }

    /**
     * Validates that the current component is properly sized and positioned within its parent.
     * <p>
     * This method checks that the dimensions of the component do not exceed the dimensions
     * of its parent and that the local position of the component ensures it remains entirely
     * within the bounds of the parent. If any validation check fails, an {@code IllegalStateException}
     * is thrown.
     * <p>
     * The validation includes two checks:
     * 1. The width and height of the component must not exceed the width and height of the parent.
     * 2. The component's local coordinates (relative to the parent) must define a position such
     *    that the entire component lies within the parent's area.
     * <p>
     * If the component has no parent, this method exits without performing any validation.
     *
     * @throws IllegalStateException if the component's size or position violates the constraints
     * within its parent's bounds.
     */
    private void validateWithinParent() {
        if (parent == null) return;

        Rectangle p = parent.componentBox;
        Rectangle c = this.componentBox;

        if (c.width > p.width || c.height > p.height) {
            throw new IllegalStateException(
                    "Child size (" + c.width + "x" + c.height + ") cannot be greater than parent size (" +
                            p.width + "x" + p.height + ")."
            );
        }

        if (localX < 0 || localY < 0 || localX + c.width > p.width || localY + c.height > p.height) {
            throw new IllegalStateException(
                    "Child local bounds [x=" + localX + ", y=" + localY + ", w=" + c.width + ", h=" + c.height +
                            "] must be inside parent size [w=" + p.width + ", h=" + p.height + "]."
            );
        }
    }


    public JFComponent setSize(int width, int height) {
        componentBox.setSize(width, height);
        updateAbsolutePositionFromLocal();
        invalidateLayout();
        return this;
    }

    public JFComponent setWidth(int width) {
        return setSize(width, componentBox.height);
    }

    public JFComponent setHeight(int height) {
        return setSize(componentBox.width, height);
    }

    public JFComponent setPosition(int x, int y) {
        this.localX = x;
        this.localY = y;

        // If parent is not known yet, we keep componentBox.x/y as the local coords for now.
        // Once attached (init), we convert to absolute.
        if (parent == null) {
            componentBox.setLocation(localX, localY);
            return this;
        }

        updateAbsolutePositionFromLocal();
        validateWithinParent();
        invalidateLayout();
        return this;
    }

    public JFComponent setX(int x) {
        return setPosition(x, this.localY);
    }

    public JFComponent setY(int y) {
        return setPosition(this.localX, y);
    }

    public JFComponent setBounds(int x, int y, int width, int height) {
        setPosition(x, y);
        setSize(width, height);
        return this;
    }

    public void setLocalPositionInternal(int x, int y) {
        this.localX = x;
        this.localY = y;
        updateAbsolutePositionFromLocal();
    }

    /**
     * Adds a child component to this component's hierarchy.
     * The child component is initialized with this component as its parent
     * and is added to the internal list of children.
     *
     * @param child the child component to be added. Must not be null.
     * @return the current component instance, allowing for method chaining.
     */
    public JFComponent addChild(@NotNull JFComponent child) {

        childList.add(child);
        child.init(this);

        invalidateLayout();

        return this;
    }

    protected void invalidateLayout() {
        layoutDirty = true;

        if (parent != null)
            parent.invalidateLayout();
    }

    public final void layout() {

        if (!layoutDirty) return;

        for (JFComponent child : childList)
            child.layout();

        layoutRecalculate();

        layoutDirty = false;
    }

    public abstract void layoutRecalculate();

    public final void validateTree() {
        validateWithinParent();

        for (JFComponent child : childList)
            child.validateTree();
    }

    /**
     * Draws the component and its child components in the component hierarchy.
     * This method first renders the visual representation of the current component
     * by invoking the {@code design} method. It then iterates through the list of child
     * components, invoking their {@code draw} methods to ensure that all descendant
     * components in the hierarchy are rendered recursively.
     * <p>
     * Subclasses must override the {@code design} method to define the component's
     * appearance, as this method relies on the {@code design} implementation.
     */
    public void draw(Graphics g) {
        design(g);

        for (JFComponent child : childList) {
            child.draw(g);
        }
    }

    /**
     * Renders the visual representation of the component.
     * This method is called to paint the component on the screen or its graphical context.
     * Each subclass must provide an implementation to define how the component should appear.
     */
    public abstract void design(Graphics g);

}
