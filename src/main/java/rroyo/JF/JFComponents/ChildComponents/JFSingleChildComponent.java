package rroyo.JF.JFComponents.ChildComponents;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Public contract for components that can host exactly one child.
 *
 * @param <T> concrete component type returned for fluent composition
 */
public interface JFSingleChildComponent<T extends JFComponent> extends JFChildComponent {

    /**
     * Small mutable holder used by single-child components to keep both the child and its ID.
     */
    class SingleChild {
        private String id;
        private JFComponent child;

        public String getId() {
            return id;
        }

        public JFComponent getChild() {
            return child;
        }

        public void set(String id, JFComponent child) {
            this.id = id;
            this.child = child;
        }

        public void clear() {
            this.id = null;
            this.child = null;
        }
    }

    /**
     * Returns the mutable single-child holder owned by the component.
     *
     * @return single-child holder
     */
    SingleChild getChildStore();

    @Override
    default List<JFComponent> getChildList() {
        JFComponent child = getChildStore().getChild();
        return child == null ? Collections.emptyList() : Collections.singletonList(child);
    }

    /**
     * Sets or replaces the single child owned by the component.
     *
     * @param child child component to mount
     * @return current component
     */
    @SuppressWarnings("unchecked")
    default T addChild(@NotNull JFComponent child) {
        return addChild(null, child);
    }

    /**
     * Sets or replaces the single child with an optional explicit ID.
     *
     * @param id explicit child ID, or {@code null} to generate one
     * @param child child component to mount
     * @return current component
     */
    @SuppressWarnings("unchecked")
    default T addChild(@Nullable String id, @NotNull JFComponent child) {
        JFComponent parent = (JFComponent) this;
        JFComponent previousChild = getChildStore().getChild();
        if (previousChild != null) {
            previousChild.detachFromParent();
            getChildStore().clear();
        }

        JFComponent nonNullChild = Objects.requireNonNull(child, "Cannot add a null child component");
        parent.validateMountableComponent(nonNullChild);

        String childId = parent.resolveMountedComponentID(id, nonNullChild);
        getChildStore().set(childId, nonNullChild);
        nonNullChild.applyMountedID(childId, id != null);
        nonNullChild.mount(parent, parent.getWindow());
        nonNullChild.ensureMountedBranchHasUniqueIDs();
        parent.markLayoutDirty();
        return (T) this;
    }

    /**
     * Returns the single child.
     *
     * @return child component, or {@code null} when empty
     */
    default JFComponent getChild() {
        return getChildStore().getChild();
    }

    /**
     * Returns the ID assigned to the single child.
     *
     * @return child ID, or {@code null} when empty
     */
    default String getChildID() {
        return getChildStore().getId();
    }

    @Override
    default void updateChildID(@NotNull JFComponent child, String oldId, @NotNull String newId) {
        if (getChildStore().getChild() == child) {
            getChildStore().set(newId, child);
        }
    }

    @Override
    default void clearChildren() {
        JFComponent child = getChildStore().getChild();
        if (child != null) {
            child.detachFromParent();
        }
        getChildStore().clear();
        ((JFComponent) this).markLayoutDirty();
    }
}
