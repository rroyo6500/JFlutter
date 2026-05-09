package rroyo.JF.JFComponents.ChildComponents;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Public contract for components that can host multiple children.
 *
 * @param <T> concrete component type returned for fluent composition
 */
public interface JFMultiChildComponent<T extends JFComponent> extends JFChildComponent {

    /**
     * Returns the mutable child map owned by the component.
     * <p>
     * Keys are component IDs and values are the direct children stored in insertion order.
     *
     * @return direct-child map
     */
    Map<String, JFComponent> getChildMap();

    @Override
    default List<JFComponent> getChildList() {
        return List.copyOf(getChildMap().values());
    }

    /**
     * Adds a single child to the component.
     *
     * @param child child to attach
     * @return current component
     */
    @SuppressWarnings("unchecked")
    default T addChild(@NotNull JFComponent child) {
        return addChild(null, child);
    }

    /**
     * Adds a single child with an optional explicit ID.
     *
     * @param id explicit child ID, or {@code null} to generate one
     * @param child child to attach
     * @return current component
     */
    @SuppressWarnings("unchecked")
    default T addChild(@Nullable String id, @NotNull JFComponent child) {
        JFComponent parent = (JFComponent) this;
        JFComponent nonNullChild = Objects.requireNonNull(child, "Cannot add a null child component");
        parent.validateMountableComponent(nonNullChild);

        String childId = parent.resolveMountedComponentID(id, nonNullChild);
        if (getChildMap().containsKey(childId)) {
            throw new IllegalArgumentException("A child with ID '" + childId + "' already exists in " + parent.getClass().getSimpleName());
        }

        getChildMap().put(childId, nonNullChild);
        nonNullChild.applyMountedID(childId, id != null);
        nonNullChild.mount(parent, parent.getWindow());
        nonNullChild.ensureMountedBranchHasUniqueIDs();
        parent.markLayoutDirty();
        return (T) this;
    }

    /**
     * Adds one or more children to the component in the provided order.
     *
     * @param children children to attach
     * @return current component
     */
    @SuppressWarnings("unchecked")
    default T addChilds(@NotNull JFComponent... children) {
        for (JFComponent child : children) {
            addChild(child);
        }
        return (T) this;
    }

    /**
     * Returns the child registered with the supplied ID.
     *
     * @param id child ID
     * @return matching child, or {@code null} when missing
     */
    default JFComponent getChild(@NotNull String id) {
        return getChildMap().get(id);
    }

    @Override
    default void updateChildID(@NotNull JFComponent child, String oldId, @NotNull String newId) {
        if (oldId != null) {
            getChildMap().remove(oldId);
        }
        getChildMap().put(newId, child);
    }

    @Override
    default void clearChildren() {
        for (JFComponent child : getChildList()) {
            child.detachFromParent();
        }
        getChildMap().clear();
        ((JFComponent) this).markLayoutDirty();
    }
}
