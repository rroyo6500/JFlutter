package rroyo.JF.JFComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.SimpleComponents.JFWindow;

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
    protected JFComponent parent;

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
    protected final List<JFComponent> childList = new ArrayList<>();

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
    protected final Rectangle componentBox = new Rectangle(0, 0, 0, 0);

    private boolean layoutDirty = true;

    protected boolean layoutRequireChild = false;

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
     * Constructs a new JFComponent with a specified layout requirement for child elements.
     * <br>
     * This constructor allows the caller to specify whether the layout mechanism
     * should require at least one child component to function properly.
     *
     * @param layoutRequireChild a boolean value indicating whether layout computation
     *                           requires the presence of at least one child component.
     *                           If true, the layout will enforce this requirement.
     */
    protected JFComponent(boolean layoutRequireChild) {
        this.layoutRequireChild = layoutRequireChild;
    }

    public JFComponent getParent() {
        return parent;
    }

    public List<JFComponent> getChildList() {
        return childList;
    }

    public JFComponent getChild(int index) {
        return childList.get(index);
    }

    public int getWidth() {
        return componentBox.width;
    }

    public int getHeight() {
        return componentBox.height;
    }

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
    protected void init(@NotNull JFComponent parent) {
        if (this.parent != null)
            throw new IllegalStateException("Component already has a parent.");

        this.parent = parent;
        updateAbsolutePositionFromLocal();
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

    /**
     * Sets the size of the component by updating its width and height dimensions.
     * This method modifies the dimensions of the component's bounding box and
     * marks the layout as invalid, ensuring that the component's layout is
     * recalculated when necessary.
     *
     * @param width  the new width of the component, in pixels.
     * @param height the new height of the component, in pixels.
     * @return the current instance of {@code JFComponent}, allowing for method chaining.
     */
    public JFComponent setSize(int width, int height) {
        componentBox.setSize(width, height);
        invalidateLayout();
        return this;
    }

    /**
     * Sets the width of the component by updating the width dimension
     * while preserving the current height. This method modifies the
     * dimensions of the component's bounding box and marks the layout
     * as invalid, ensuring that the component's layout is recalculated
     * if necessary.
     *
     * @param width the new width of the component, in pixels.
     * @return the current instance of {@code JFComponent}, allowing for method chaining.
     */
    public JFComponent setWidth(int width) {
        return setSize(width, componentBox.height);
    }

    /**
     * Sets the height of the component while retaining its current width.
     * This method updates the height dimension of the component's bounding box
     * and marks the layout as invalid, ensuring that the layout is recalculated
     * when necessary.
     *
     * @param height the new height of the component, in pixels.
     * @return the current instance of {@code JFComponent}, allowing for method chaining.
     */
    public JFComponent setHeight(int height) {
        return setSize(componentBox.width, height);
    }

    /**
     * Sets the position of the component using local x and y coordinates.
     * Updates the component's absolute position relative to its parent
     * and invalidates the layout.
     *
     * @param x the x-coordinate of the component's new local position.
     * @param y the y-coordinate of the component's new local position.
     * @return the current instance of {@code JFComponent}, allowing for method chaining.
     */
    public JFComponent setPosition(int x, int y) {
        this.localX = x;
        this.localY = y;

        updateAbsolutePositionFromLocal();
        invalidateLayout();
        return this;
    }

    /**
     * Adds a child component to this component's hierarchy.
     * The child component is initialized with this component as its parent
     * and is added to the internal list of children.
     *
     * @param child the child component to be added. Must not be null.
     */
    public JFComponent addChild(@NotNull JFComponent child) {
        if (child.getClass() == JFWindow.class) throw new RuntimeException("Cannot add JFWindow to a JFComponent");

        childList.add(child);
        child.init(this);
        invalidateLayout();
        return this;
    }

    /**
     * Marks the layout of this component as invalid and recursively propagates
     * the invalidation to its parent component, if any.
     * <br>
     * This method sets the internal `layoutDirty` flag to true, indicating
     * that the component's layout needs to be recalculated. If the component
     * has a parent, the parent component's layout is also invalidated by
     * recursively calling its `invalidateLayout` method.
     * <br>
     * This mechanism ensures that layout changes propagate upward through
     * the component hierarchy, ensuring all affected components are updated
     * during the next layout recalculation.
     */
    protected final void invalidateLayout() {
        layoutDirty = true;

        if (parent != null)
            parent.invalidateLayout();
    }

    /**
     * Performs the layout operations for the current component and its children.
     * <br>
     * This method checks whether the layout is marked as dirty. If the layout is up-to-date,
     * the method terminates early. Otherwise, it executes the layout procedures for the
     * component and its children, ensuring proper recalculations and size adjustments.
     * <br>
     * The layout process includes: <br>
     * - Propagating layout operations to child components if required. <br>
     * - Recalculating layout properties for the current component using the defined logic. <br>
     * - Adjusting the component's size if its width or height is marked as infinite. <br>
     * - Setting the layout as up-to-date (not dirty) upon completion. <br>
     * <br>
     * This method is protected, final, and intended to ensure consistent and efficient layout
     * calculations across a component hierarchy.
     */
    protected final void layout() {

        if (!layoutDirty) return;

        if (layoutRequireChild)
            for (JFComponent child : childList)
                child.layout();

        layoutRecalculate();

        for (JFComponent child : childList)
            child.layout();

        layoutDirty = false;
    }

    /**
     * Recalculates the layout of the component. This method should be implemented
     * by subclasses to define the specific recalculation logic for arranging
     * or resizing elements within the component. It is typically invoked when
     * a layout change or update is required due to external or internal constraints.
     */
    protected abstract void layoutRecalculate();

    /**
     * Recursively validates the structure and integrity of the tree
     * of components starting from the current component.
     * <br>
     * This method ensures that the current component is validated within
     * the context of its parent and subsequently validates all its child
     * components in depth-first order.
     * <br>
     * Note:
     * - The method is protected and can only be accessed within its package
     *   or by subclasses.
     * - It is declared final, indicating it cannot be overridden by subclasses.
     */
    protected final void validateTree() {
        validateWithinParent();

        for (JFComponent child : childList)
            child.validateTree();
    }

    /**
     * Retrieves the closest component in the tree that matches one of the specified classes.
     * The method traverses the hierarchy of the current component's parent objects to find the nearest matching component.
     *
     * @param componentClass one or more classes that the desired component should match
     * @return the closest matching component in the hierarchy, or null if no matching component is found
     */
    @SafeVarargs
    protected final JFComponent getComponentFromTree(Class<? extends JFComponent>... componentClass){
        JFComponent retComponent = null;
        int deep = 0;

        for (Class<? extends JFComponent> c : componentClass) {
            JFComponent comp = this.parent;
            int d = 0;

            while (comp.getClass() != c && comp.parent != null) {
                comp = comp.parent;
                d++;
            }

            if (retComponent == null) {
                retComponent = comp;
                deep = d;
            } else if (deep > d) {
                retComponent = comp;
                deep = d;
            }

        }

        return retComponent;
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
    protected void draw(Graphics g) {
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
    protected abstract void design(Graphics g);

    /**
     * Checks whether an absolute point is inside this component bounds.
     *
     * @param x absolute x
     * @param y absolute y
     * @return true when the point is inside this component
     */
    public final boolean containsPoint(int x, int y) {
        return componentBox.contains(x, y);
    }

    /**
     * Finds the top-most component at a given absolute point.
     * Children are evaluated in reverse draw order.
     *
     * @param x absolute x
     * @param y absolute y
     * @return the deepest component containing the point, or null
     */
    public final JFComponent findTopMostAt(int x, int y) {
        for (int i = childList.size() - 1; i >= 0; i--) {
            JFComponent child = childList.get(i);
            JFComponent target = child.findTopMostAt(x, y);
            if (target != null) return target;
        }

        return containsPoint(x, y) ? this : null;
    }

    @Override
    public String toString() {
        return String.format("%s{width:%d, height:%d}", this.getClass().getSimpleName(), componentBox.width, componentBox.height);
    }
}
