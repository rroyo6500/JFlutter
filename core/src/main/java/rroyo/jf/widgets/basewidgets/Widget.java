package rroyo.jf.widgets.basewidgets;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rroyo.jf.widgets.childcomponent.ChildComponent;
import rroyo.jf.widgets.simplewidgets.Window;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Widget {

    @Getter @Setter
    protected Widget father;

    @Getter
    protected Window window;

    @Getter
    protected String id;
    private boolean userDefinedID = false;

    protected final Rectangle bounds = new Rectangle();
    private boolean layoutDirty = true;

    @Getter @Setter
    private boolean visible = true;

    @Getter
    private boolean active = true;

    @Getter @Setter
    private boolean overflow = false;

    @Getter @Setter
    private boolean clipChildrenToBounds = false;

    protected boolean layoutRequiredChild = false;
    protected Point local = new Point();

    @Getter
    private final Map<String, Widget> childrenMap = new LinkedHashMap<>();

    @Getter
    private final rroyo.jf.events.EventDispatcher eventDispatcher = new rroyo.jf.events.EventDispatcher();

    public Widget() {}

    protected Widget(boolean layoutRequiredChild) {
        this.layoutRequiredChild = layoutRequiredChild;
    }

    // --- Lifecycle & Layout ---

    protected abstract void layoutRecalculate();
    protected abstract void design(Graphics g);

    protected final void layout() {
        if (!participatesInLayout() || !layoutDirty) return;

        int layoutPasses = 0;
        do {
            layoutDirty = false;

            if (layoutRequiredChild) {
                for (Widget child : getTraversalChildren()) {
                    if (child.participatesInLayout()) child.layout();
                }
            }

            layoutRecalculate();

            for (Widget child : getTraversalChildren()) {
                if (child.participatesInLayout()) child.layout();
            }

            if (++layoutPasses > 100) {
                throw new IllegalStateException("Layout did not stabilize for " + getClass().getSimpleName() + ".");
            }
        } while (layoutDirty);
    }

    protected void draw(Graphics g) {
        if (!canDraw()) return;

        Graphics2D g2d = (Graphics2D) g;

        Shape originalClip = g2d.getClip();
        AffineTransform originalTransform = g2d.getTransform();

        g2d.clipRect(getAbsPosition().x, getAbsPosition().y, getSize().width, getSize().height);

        g2d.translate(getAbsPosition().x, getAbsPosition().y);
        design(g2d);
        g2d.setTransform(originalTransform);

        for (Widget child : getTraversalChildren()) {
            if (child.canDraw()) child.draw(g2d);
        }

        g2d.setClip(originalClip);
    }

    // --- Tree & Mounting Operations ---

    public void init(@NotNull Widget father, @Nullable Window window) {
        setWindow(window);
        setFather(father);

        updateAbsPosition();
        invalidateLayout();
    }

    public void setWindow(@Nullable Window window) {
        this.window = window;
        for (Widget child : getTraversalChildren()) {
            child.setWindow(window);
        }
    }

    public void detachFromFather() {
        if (father != null && father.childrenMap.containsValue(this)) {
            father.childrenMap.values().remove(this);
        }
        this.father = null;
        this.window = null;
    }

    protected final void validateTree() {
        if (!participatesInLayout()) return;

        validateWithinParent();

        for (Widget child : getTraversalChildren()) {
            if (child.participatesInLayout()) {
                child.validateTree();
            }
        }
    }

    private void updateAbsPosition() {
        if (father != null) {
            bounds.setLocation(father.getAbsPosition().x + local.x, father.getAbsPosition().y + local.y);
        }
        for (Widget child : getTraversalChildren()) {
            child.updateAbsPosition();
        }
    }

    private void validateWithinParent() {
        if (!isActive() || father == null || isOverflow()) return;

        Rectangle p = father.bounds;
        Rectangle c = this.bounds;

        if (c.width > p.width || c.height > p.height) {
            throw new IllegalStateException(
                    "Child size (" + c.width + "x" + c.height + ") cannot be greater than parent size (" +
                            p.width + "x" + p.height + ")."
            );
        }

        if (local.x < 0 || local.y < 0 || local.x + c.width > p.width || local.y + c.height > p.height) {
            throw new IllegalStateException(
                    "Child local bounds [x=" + local.x + ", y=" + local.y + ", w=" + c.width + ", h=" + c.height +
                            "] must be inside parent size [w=" + p.width + ", h=" + p.height + "]."
            );
        }
    }

    @SafeVarargs
    protected final Widget getComponentFromTree(
            @NotNull Class<? extends Widget>... componentTypes
    ) {
        Objects.requireNonNull(componentTypes, "Component types cannot be null");

        List<Class<? extends Widget>> types = new ArrayList<>(componentTypes.length);
        types.add(Window.class);

        Widget current = this.father;

        while (current != null) {

            for (Class<? extends Widget> type : types) {

                Objects.requireNonNull(
                        type,
                        "Component type cannot be null"
                );

                if (type.isInstance(current)) {
                    return current;
                }
            }

            current = current.father;
        }

        return null;
    }

    // --- Identification & Hierarchy Management ---

    public final String resolveMountedComponentID(@Nullable String requestedId, Widget child) {
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

    private void assertIDIsUniqueInTree(String id, Widget ignoredComponent) {
        if (findDuplicateIDInTree(id, ignoredComponent) != null) {
            throw new IllegalArgumentException("A component with ID '" + id + "' already exists in this component tree.");
        }
    }

    public final void applyMountedID(@NotNull String id, boolean userDefined) {
        this.id = validateComponentID(id);
        this.userDefinedID = userDefined || this.userDefinedID;
    }

    private String generateUniqueRandomID(Widget ignoredComponent) {
        String randomId;
        do {
            randomId = Long.toUnsignedString(ThreadLocalRandom.current().nextLong());
        } while (findDuplicateIDInTree(randomId, ignoredComponent) != null);

        return randomId;
    }

    private Widget findDuplicateIDInTree(String id, Widget ignoredComponent) {
        return getRoot().findComponentByID(id, ignoredComponent);
    }

    private Widget findComponentByID(String id, Widget ignoredComponent) {
        if (this != ignoredComponent && id.equals(this.id)) return this;

        for (Widget child : getTraversalChildren()) {
            Widget match = child.findComponentByID(id, ignoredComponent);
            if (match != null) return match;
        }

        return null;
    }

    private Widget getRoot() {
        Widget root = this;
        while (root.father != null) {
            root = root.father;
        }
        return root;
    }

    public final void ensureMountedBranchHasUniqueIDs() {
        if (id != null) {
            if (userDefinedID) {
                assertIDIsUniqueInTree(id, this);
            } else if (findDuplicateIDInTree(id, this) != null) {
                updateRegisteredID(generateUniqueRandomID(this));
            }
        }

        for (Widget child : new ArrayList<>(getTraversalChildren())) {
            child.ensureMountedBranchHasUniqueIDs();
        }
    }

    private void updateRegisteredID(String newId) {
        String oldId = this.id;
        if (father instanceof ChildComponent childParent) {
            childParent.updateChildID(this, oldId, newId);
        }
        this.id = newId;
    }

    public Collection<Widget> getTraversalChildren() {
        return childrenMap.values();
    }

    public List<Widget> getTraversalChildrenInReverseOrder() {
        List<Widget> list = new ArrayList<>(childrenMap.values());
        Collections.reverse(list);
        return list;
    }

    public final void markLayoutDirty() {
        invalidateLayout();
    }

    protected final void invalidateLayout() {
        layoutDirty = true;
        if (father != null) {
            father.invalidateLayout();
        }
    }

    private void invalidateChildLayouts() {
        for (Widget child : getTraversalChildren()) {
            child.layoutDirty = true;
            child.invalidateChildLayouts();
        }
    }

    public final boolean containsPoint(int x, int y) {
        return canDraw() && bounds.contains(x, y);
    }

    public final Widget findTopMostAt(int x, int y) {
        if (!canDraw()) return null;
        if (clipChildrenToBounds && !bounds.contains(x, y)) return null;

        for (Widget child : getTraversalChildrenInReverseOrder()) {
            Widget target = child.findTopMostAt(x, y);
            if (target != null) return target;
        }

        return containsPoint(x, y) ? this : null;
    }

    public final void drawTree(Graphics g) {
        draw(g);
    }

    public final void validateMountableComponent(@NotNull Widget component) {
        Objects.requireNonNull(component, "Cannot add a null child component");
        if (component instanceof Window) {
            throw new IllegalArgumentException("Cannot add Window to a Widget");
        }
    }

    public void repaint() {
        window.repaint();
    }

    // --- Getters & Setters ---

    public Widget setId(@NotNull String id) {
        String newId = validateComponentID(id);
        if (Objects.equals(this.id, newId)) return this;

        Widget duplicate = findDuplicateIDInTree(newId, this);
        if (duplicate != null) {
            throw new IllegalArgumentException("Component with ID '" + id + "' already exists in this component tree.");
        }

        updateRegisteredID(newId);
        userDefinedID = true;
        return this;
    }

    public final boolean canDraw() {
        return active && visible;
    }

    public Dimension getSize() {
        return new Dimension(bounds.width, bounds.height);
    }

    public Widget setSize(Dimension size) {
        return setSize(size.width, size.height);
    }

    public Widget setSize(int width, int height) {
        if (bounds.width == width && bounds.height == height) return this;

        bounds.setSize(width, height);
        invalidateChildLayouts();
        invalidateLayout();
        return this;
    }

    public Point getPosition() {
        return new Point(local.x, local.y);
    }

    public Point getAbsPosition() {
        return new Point(bounds.x, bounds.y);
    }

    public Widget setPosition(Point position) {
        return setPosition(position.x, position.y);
    }

    public Widget setPosition(int x, int y) {
        if (this.local.x == x && this.local.y == y) return this;

        this.local.x = x;
        this.local.y = y;

        updateAbsPosition();
        invalidateLayout();
        return this;
    }

    public Rectangle getBounds() {
        return new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Widget setActive(boolean active) {
        if (this.active == active) return this;
        this.active = active;
        return this;
    }

    protected final boolean participatesInLayout() {
        return active;
    }


    // toString

    @Override
    public String toString() {
        if (this instanceof ChildComponent cc) {
            return cc.getComponentTree();
        }
        return String.format("%s [id=%s]", getClass().getSimpleName(), getId());
    }
}