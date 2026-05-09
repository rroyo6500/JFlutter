package rroyo.JF.JFComponents.BaseComponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rroyo.JF.JFComponents.ChildComponents.JFChildComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFWindow;
import rroyo.JUtils.Utils.Core.Validator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Abstract base class for every visual node managed by the framework.
 * <p>
 * {@code JFComponent} is responsible for the core mechanics shared by all components:
 * parent-child relationships, absolute and local geometry, lazy layout invalidation,
 * recursive rendering, and hit detection. Concrete subclasses only need to implement
 * their own layout logic and drawing routine on top of this foundation.
 * <p>
 * The class is intentionally lightweight but central. Most higher-level behaviors in the
 * framework, such as hover resolution, event routing and nested layout composition, rely on
 * the invariants maintained here.
 *
 * @author rroyo
 */
public abstract class JFComponent {

    /**
     * Immediate parent of this component inside the component tree.
     * <p>
     * A {@code null} value indicates that the component is currently detached or is itself
     * the root node. The parent reference is used to calculate absolute coordinates, to walk
     * up the tree looking for ancestor context, and to propagate layout invalidation.
     */
    protected JFComponent parent;

    /**
     * Window that owns the component tree containing this component.
     * <p>
     * Components use this reference when they need access to root-level services such as repaint
     * scheduling or overlay registration. Detached components receive the reference when attached.
     */
    protected JFWindow window;

    /**
     * ID used by the parent to locate this component inside the component tree.
     * <p>
     * The value is assigned when the component is attached. Callers may also provide one through
     * child-management APIs or update it later with {@link #setID(String)}.
     */
    protected String id;
    private boolean userDefinedID = false;

    /**
     * Absolute bounding rectangle of the component in window coordinates.
     * <p>
     * The rectangle stores both the current size and the current absolute position. Even though
     * the coordinates are absolute, components are typically configured with local positions;
     * those local coordinates are converted into absolute ones whenever the tree is updated.
     */
    protected final Rectangle componentBox = new Rectangle(0, 0, 0, 0);

    /**
     * Flag indicating whether layout data must be recalculated before the next render pass.
     */
    private boolean layoutDirty = true;

    /**
     * Controls whether the component should be painted.
     * <p>
     * Invisible components still participate in layout as long as they remain active.
     */
    private boolean visible = true;

    /**
     * Controls whether the component participates in layout and rendering.
     * <p>
     * Inactive components are ignored by layout containers, skipped during validation and never
     * drawn or hit-tested.
     */
    private boolean active = true;

    /**
     * Allows this component to extend outside parent bounds without triggering validation errors.
     */
    private boolean overflowAllowed = false;
    /**
     * Restricts child drawing and hit testing to this component bounds.
     */
    private boolean clipChildrenToBounds = false;

    /**
     * Indicates whether children must be laid out before this component recalculates itself.
     * <p>
     * Some containers depend on the final size of their children to compute their own size,
     * while others determine their own bounds first and then position children afterwards.
     */
    protected boolean layoutRequireChild = false;

    /**
     * X coordinate relative to the parent's origin.
     * <p>
     * This is the local position explicitly assigned to the component. The final absolute
     * position is obtained by adding this value to the absolute X coordinate of the parent.
     */
    protected int localX = 0;

    /**
     * Y coordinate relative to the parent's origin.
     * <p>
     * Like {@link #localX}, this value is local to the parent. It is kept separate from the
     * absolute coordinates stored in {@link #componentBox} so layout code can reason in the
     * natural coordinate system of each container.
     */
    protected int localY = 0;

    /**
     * Creates a detached component with an empty child list and zero-sized bounds.
     */
    public JFComponent() {}

    /**
     * Creates a component and declares whether its layout depends on child layout running first.
     *
     * @param layoutRequireChild whether children must be laid out before this component
     */
    protected JFComponent(boolean layoutRequireChild) {
        this.layoutRequireChild = layoutRequireChild;
    }

    /**
     * Returns the internal absolute bounding box used by the framework.
     * <p>
     * The method is protected because subclasses sometimes need direct geometry access for
     * custom layout or drawing calculations, while callers outside the framework are encouraged
     * to use the public width, height and hit-test helpers instead.
     *
     * @return absolute component rectangle
     */
    public Rectangle getComponentBox() {
        return componentBox;
    }

    /**
     * Returns the direct parent component in the current component tree.
     *
     * @return parent component, or {@code null} if the component is detached or is the root
     */
    public JFComponent getParent() {
        return parent;
    }

    /**
     * Updates the parent reference used for ancestry traversal and absolute positioning.
     *
     * @param parent new parent component
     * @return current component for fluent calls
     */
    public JFComponent setParent(@NotNull JFComponent parent) {
        this.parent = parent;
        return this;
    }

    /**
     * Stores the root window that owns this component tree and propagates it to descendants.
     *
     * @param window owning framework window, or {@code null} while composing a detached subtree
     * @return current component for fluent calls
     */
    public JFComponent setWindow(@Nullable JFWindow window) {
        this.window = window;

        for (JFComponent child : getTraversalChildren()) {
            child.setWindow(window);
        }

        return this;
    }

    /**
     * Returns the root window that owns this component tree.
     *
     * @return owning window, or {@code null} while detached from a window
     */
    public JFWindow getWindow() {
        return window;
    }

    /**
     * Returns the component ID used by its parent.
     *
     * @return component ID, or {@code null} when detached and not explicitly assigned
     */
    public String getID() {
        return id;
    }

    /**
     * Updates the component ID and re-registers it in the parent's child storage.
     *
     * @param id new non-blank component ID
     * @return current component for fluent calls
     */
    public JFComponent setID(@NotNull String id) {
        String newId = validateComponentID(id);
        if (Objects.equals(this.id, newId)) return this;

        JFComponent duplicate = findDuplicateIDInTree(newId, this);
        if (duplicate != null) {
            throw new IllegalArgumentException("A component with ID '" + newId + "' already exists in this component tree.");
        }

        updateRegisteredID(newId);
        userDefinedID = true;
        return this;
    }

    /**
     * Returns the current component width in pixels.
     *
     * @return current width
     */
    public int getWidth() {
        return componentBox.width;
    }

    /**
     * Returns the current component height in pixels.
     *
     * @return current height
     */
    public int getHeight() {
        return componentBox.height;
    }

    /**
     * Indicates whether the component should currently be painted.
     *
     * @return {@code true} when the component is visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Indicates whether the component is currently enabled for layout and rendering.
     *
     * @return {@code true} when the component is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns whether the component may render outside the bounds of its parent.
     *
     * @return {@code true} when overflow validation is disabled for this component
     */
    public boolean isOverflowAllowed() {
        return overflowAllowed;
    }

    /**
     * Returns whether child rendering and hit testing should be clipped to this component box.
     *
     * @return {@code true} when child clipping is enabled
     */
    public boolean isClipChildrenToBounds() {
        return clipChildrenToBounds;
    }

    /**
     * Changes whether this component should be painted.
     *
     * @param visible new visibility state
     * @return current component for fluent calls
     */
    public JFComponent setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    /**
     * Changes whether this component participates in layout and rendering.
     *
     * @param active new activation state
     * @return current component for fluent calls
     */
    public JFComponent setActive(boolean active) {
        if (this.active == active)
            return this;

        this.active = active;
        invalidateLayout();
        return this;
    }

    /**
     * Controls whether parent-bound validation should be skipped for this component.
     *
     * @param overflowAllowed new overflow policy
     * @return current component for fluent calls
     */
    public JFComponent setOverflowAllowed(boolean overflowAllowed) {
        this.overflowAllowed = overflowAllowed;
        return this;
    }

    /**
     * Controls whether children should only render and receive hits inside this component bounds.
     *
     * @param clipChildrenToBounds new clipping policy
     * @return current component for fluent calls
     */
    public JFComponent setClipChildrenToBounds(boolean clipChildrenToBounds) {
        this.clipChildrenToBounds = clipChildrenToBounds;
        return this;
    }

    /**
     * Returns whether the component should participate in layout calculations.
     *
     * @return {@code true} when the component is active
     */
    protected final boolean participatesInLayout() {
        return active;
    }

    /**
     * Returns whether the component should be traversed during drawing and hit detection.
     *
     * @return {@code true} when the component is both active and visible
     */
    protected final boolean canDraw() {
        return active && visible;
    }

    /**
     * Mounts this component inside a component tree.
     *
     * @param parent parent component
     * @param window owning window, or {@code null} while mounting inside a detached subtree
     * @throws IllegalStateException if the component was already attached to a different parent
     */
    public final void mount(@NotNull JFComponent parent, @Nullable JFWindow window) {
        Validator.assertTrue(this.parent == null, "Component already has a parent.");

        setWindow(window);
        setParent(parent);
        updateAbsolutePositionFromLocal();
        invalidateLayout();
    }

    /**
     * Recomputes absolute coordinates from the stored local coordinates and propagates the update
     * recursively to the entire subtree.
     * <p>
     * This keeps descendants spatially in sync when a parent moves. The method does not change
     * sizes; it only updates the absolute location portion of each descendant rectangle.
     */
    private void updateAbsolutePositionFromLocal() {
        if (parent != null)
            componentBox.setLocation(parent.componentBox.x + localX, parent.componentBox.y + localY);

        for (JFComponent child : getTraversalChildren()) {
            child.updateAbsolutePositionFromLocal();
        }
    }

    /**
     * Verifies that the component remains fully inside the bounds of its parent.
     * <p>
     * Validation happens after layout and before painting so layout bugs are detected early.
     * The method checks both size and position constraints. Detached components are ignored,
     * because they have no parent bounds to validate against yet.
     *
     * @throws IllegalStateException when the component exceeds or escapes the parent bounds
     */
    private void validateWithinParent() {
        if (!participatesInLayout() || parent == null) return;
        if (overflowAllowed) return;

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
     * Updates the size stored in the component bounding box and invalidates layout.
     * <p>
     * The invalidation step is important because parent containers may depend on this component's
     * size to recompute their own layout.
     *
     * @param width new width in pixels
     * @param height new height in pixels
     * @return current component for fluent calls
     */
    public JFComponent setSize(int width, int height) {
        if (componentBox.width == width && componentBox.height == height)
            return this;

        componentBox.setSize(width, height);
        invalidateChildLayouts();
        invalidateLayout();
        return this;
    }

    /**
     * Convenience helper that changes only the width while preserving height.
     *
     * @param width new width in pixels
     * @return current component for fluent calls
     */
    public JFComponent setWidth(int width) {
        return setSize(width, componentBox.height);
    }

    /**
     * Convenience helper that changes only the height while preserving width.
     *
     * @param height new height in pixels
     * @return current component for fluent calls
     */
    public JFComponent setHeight(int height) {
        return setSize(componentBox.width, height);
    }

    /**
     * Changes the local position of the component inside its parent.
     * <p>
     * After storing the new local coordinates, the method recomputes the absolute position of
     * the whole subtree and invalidates layout so dependent containers can react if necessary.
     *
     * @param x new local X coordinate
     * @param y new local Y coordinate
     * @return current component for fluent calls
     */
    public JFComponent setPosition(int x, int y) {
        if (this.localX == x && this.localY == y)
            return this;

        this.localX = x;
        this.localY = y;

        updateAbsolutePositionFromLocal();
        invalidateLayout();
        return this;
    }

    public final void validateMountableComponent(@NotNull JFComponent component) {
        Objects.requireNonNull(component, "Cannot add a null child component");
        if (component.getClass() == JFWindow.class) throw new RuntimeException("Cannot add JFWindow to a JFComponent");
    }

    public final String resolveMountedComponentID(@Nullable String requestedId, JFComponent child) {
        if (requestedId != null) {
            String userId = validateComponentID(requestedId);
            assertIDIsUniqueInTree(userId, child);
            return userId;
        }

        if (child.id != null && !child.id.isBlank()) {
            String currentId = validateComponentID(child.id);
            if (child.userDefinedID) {
                assertIDIsUniqueInTree(currentId, child);
                return currentId;
            }

            if (findDuplicateIDInTree(currentId, child) == null) {
                return currentId;
            }
        }

        return generateUniqueRandomID(child);
    }

    public static String validateComponentID(@NotNull String id) {
        String nonBlankId = Objects.requireNonNull(id, "Component ID cannot be null").trim();
        if (nonBlankId.isEmpty()) {
            throw new IllegalArgumentException("Component ID cannot be blank.");
        }
        return nonBlankId;
    }

    private void assertIDIsUniqueInTree(String id, JFComponent ignoredComponent) {
        if (findDuplicateIDInTree(id, ignoredComponent) != null) {
            throw new IllegalArgumentException("A component with ID '" + id + "' already exists in this component tree.");
        }
    }

    public final void applyMountedID(@NotNull String id, boolean userDefined) {
        this.id = validateComponentID(id);
        this.userDefinedID = userDefined || this.userDefinedID;
    }

    public final void detachFromParent() {
        parent = null;
    }

    public final void markLayoutDirty() {
        invalidateLayout();
    }

    private String generateUniqueRandomID(JFComponent ignoredComponent) {
        String randomId;
        do {
            randomId = Long.toUnsignedString(ThreadLocalRandom.current().nextLong());
        } while (findDuplicateIDInTree(randomId, ignoredComponent) != null);

        return randomId;
    }

    private JFComponent findDuplicateIDInTree(String id, JFComponent ignoredComponent) {
        JFComponent root = getRoot();
        return root.findComponentByID(id, ignoredComponent);
    }

    private JFComponent findComponentByID(String id, JFComponent ignoredComponent) {
        if (this != ignoredComponent && id.equals(this.id)) return this;

        for (JFComponent child : getTraversalChildren()) {
            JFComponent match = child.findComponentByID(id, ignoredComponent);
            if (match != null) return match;
        }

        return null;
    }

    private JFComponent getRoot() {
        JFComponent root = this;
        while (root.parent != null) {
            root = root.parent;
        }
        return root;
    }

    private void updateRegisteredID(String newId) {
        String oldId = this.id;
        if (parent instanceof JFChildComponent childParent) {
            childParent.updateChildID(this, oldId, newId);
        }

        this.id = newId;
    }

    public final void ensureMountedBranchHasUniqueIDs() {
        if (id != null) {
            if (userDefinedID) {
                assertIDIsUniqueInTree(id, this);
            } else if (findDuplicateIDInTree(id, this) != null) {
                updateRegisteredID(generateUniqueRandomID(this));
            }
        }

        for (JFComponent child : new ArrayList<>(getTraversalChildren())) {
            child.ensureMountedBranchHasUniqueIDs();
        }
    }

    private Collection<JFComponent> getTraversalChildren() {
        if (this instanceof JFChildComponent childComponent) {
            return childComponent.getChildList();
        }

        return Collections.emptyList();
    }

    private List<JFComponent> getTraversalChildrenInReverseOrder() {
        if (this instanceof JFChildComponent childComponent) {
            return childComponent.getChildListInReverseOrder();
        }

        return Collections.emptyList();
    }

    /**
     * Marks this component and its ancestors as requiring a new layout pass.
     * <p>
     * Layout invalidation propagates upward because a size or position change in a child may
     * influence how every ancestor container should arrange its own content.
     */
    protected final void invalidateLayout() {
        layoutDirty = true;

        if (parent != null)
            parent.invalidateLayout();
    }

    /**
     * Marks descendants as dirty when the current component changes the space they depend on.
     * <p>
     * Unlike {@link #invalidateLayout()}, this method only propagates downward.
     */
    private void invalidateChildLayouts() {
        for (JFComponent child : getTraversalChildren()) {
            child.layoutDirty = true;
            child.invalidateChildLayouts();
        }
    }

    /**
     * Executes the layout algorithm for this component and its subtree only when needed.
     * <p>
     * The method implements the common layout orchestration shared by all components:
     * optionally lay out children first, run the subclass-specific recalculation logic, then
     * ensure all children are laid out. This allows each subclass to focus only on the part
     * that is unique to its own sizing or positioning rules.
     */
    protected final void layout() {
        if (!participatesInLayout()) {
            layoutDirty = false;
            return;
        }

        if (!layoutDirty) return;

        int layoutPasses = 0;
        do {
            layoutDirty = false;

            if (layoutRequireChild)
                for (JFComponent child : getTraversalChildren())
                    if (child.participatesInLayout())
                        child.layout();

            layoutRecalculate();

            for (JFComponent child : getTraversalChildren())
                if (child.participatesInLayout())
                    child.layout();

            layoutPasses++;
            if (layoutPasses > 100)
                throw new IllegalStateException("Layout did not stabilize for " + getClass().getSimpleName() + ".");
        } while (layoutDirty);
    }

    /**
     * Executes layout for this component while limiting recursion to one selected child.
     * <p>
     * This variant is used by root containers that can retain multiple child trees but only want
     * one tree to participate in the current render cycle.
     *
     * @param loadedChild child that should participate in this layout pass
     */
    protected final void layout(JFComponent loadedChild) {
        if (!participatesInLayout()) {
            layoutDirty = false;
            return;
        }

        if (!layoutDirty) return;

        int layoutPasses = 0;
        do {
            layoutDirty = false;

            if (loadedChild != null)
                if (layoutRequireChild)
                    if (loadedChild.participatesInLayout())
                        loadedChild.layout();

            layoutRecalculate();

            if (loadedChild != null)
                if (loadedChild.participatesInLayout())
                    loadedChild.layout();

            layoutPasses++;
            if (layoutPasses > 100)
                throw new IllegalStateException("Layout did not stabilize for " + getClass().getSimpleName() + ".");
        } while (layoutDirty);
    }

    /**
     * Recalculates the component geometry according to the rules of the concrete subclass.
     * <p>
     * Implementations usually decide their own size, position children, or both.
     */
    protected abstract void layoutRecalculate();

    /**
     * Recursively validates the current component and all descendants after layout has run.
     * <p>
     * This catches layout errors close to their origin and ensures every visible node in the
     * subtree respects parent bounds before drawing begins.
     */
    protected final void validateTree() {
        if (!participatesInLayout()) return;

        validateWithinParent();

        for (JFComponent child : getTraversalChildren())
            if (child.participatesInLayout())
                child.validateTree();
    }

    /**
     * Validates this component while limiting recursive validation to one selected child.
     *
     * @param loadedChild child that should be validated with this component
     */
    protected final void validateTree(JFComponent loadedChild) {
        if (!participatesInLayout()) return;

        validateWithinParent();

        if (loadedChild != null)
            if (loadedChild.participatesInLayout())
                loadedChild.validateTree();
    }

    /**
     * Searches upward through the ancestry chain and returns the nearest component whose class
     * matches one of the requested types.
     * <p>
     * The method is useful when a layout needs context from a specific ancestor, such as a
     * container with explicit size constraints. If several candidate classes are supplied,
     * the closest match in terms of tree depth is returned.
     *
     * @param componentClass one or more ancestor classes to search for
     * @return nearest matching ancestor, or {@code null} if none of the requested types is found
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
     * Draws this component first and then all descendants in insertion order.
     * <p>
     * Subclasses implement only {@link #design(Graphics)} to paint themselves. Recursive child
     * drawing is kept centralized here so the traversal order is the same for every component.
     *
     * @param g graphics context supplied by the window
     */
    protected void draw(Graphics g) {
        if (!canDraw()) return;

        design(g);

        for (JFComponent child : getTraversalChildren()) {
            child.draw(g);
        }
    }

    /**
     * Draws this component and a single selected child tree.
     *
     * @param g graphics context supplied by the window
     * @param loadedChild child that should be painted after this component
     */
    protected void draw(Graphics g, JFComponent loadedChild) {
        if (!canDraw()) return;

        design(g);

        if (loadedChild != null)
            loadedChild.draw(g);
    }

    /**
     * Paints the visual representation of the current component only.
     *
     * @param g graphics context used for rendering
     */
    protected abstract void design(Graphics g);

    /**
     * Tests whether an absolute point lies inside the component bounds.
     *
     * @param x absolute X coordinate
     * @param y absolute Y coordinate
     * @return {@code true} when the point is inside the current bounding box
     */
    public final boolean containsPoint(int x, int y) {
        return canDraw() && componentBox.contains(x, y);
    }

    /**
     * Finds the deepest visible component whose bounds contain the supplied point.
     * <p>
     * Children are traversed in reverse order so later-drawn components win the hit test, which
     * matches the visual stacking order perceived by the user.
     *
     * @param x absolute X coordinate
     * @param y absolute Y coordinate
     * @return deepest component containing the point, or {@code null} when nothing matches
     */
    public final JFComponent findTopMostAt(int x, int y) {
        if (!canDraw()) return null;
        if (clipChildrenToBounds && !componentBox.contains(x, y)) return null;

        for (JFComponent child : getTraversalChildrenInReverseOrder()) {
            JFComponent target = child.findTopMostAt(x, y);
            if (target != null) return target;
        }

        return containsPoint(x, y) ? this : null;
    }

    /**
     * Draws the current component tree using the regular framework traversal.
     *
     * @param g graphics context used for rendering
     */
    public final void drawTree(Graphics g) {
        draw(g);
    }

    /**
     * Returns a compact textual description containing the class name and current size.
     *
     * @return short debug-oriented description
     */
    @Override
    public String toString() {
        if (this instanceof JFChildComponent JFCC) {
            return JFCC.getComponentTree();
        } else {
            return String.format("%s [id=%s]",
                    this.getClass().getSimpleName(),
                    this.getID()
            );
        }
    }
}
