package rroyo.jf.widgets.childcomponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rroyo.jf.widgets.basewidgets.Widget;

import java.util.Map;
import java.util.Objects;

public interface MultiChildComponent<T extends Widget> extends ChildComponent {

    default Map<String, Widget> getChildMap() {
        return ((Widget) this).getChildrenMap();
    }

    @SuppressWarnings("unchecked")
    default T addChild(@NotNull Widget child) {
        return addChild(null, child);
    }

    @SuppressWarnings("unchecked")
    default T addChild(@Nullable String id, @NotNull Widget child) {
        Widget father = (Widget) this;
        Widget nonNullChild = Objects.requireNonNull(child, "Cannot add a null child component");
        father.validateMountableComponent(nonNullChild);

        String childId = father.resolveMountedComponentID(id, nonNullChild);
        if (getChildMap().containsKey(childId)) {
            throw new IllegalArgumentException("A child with ID '" + childId + "' already exists in " + father.getClass().getSimpleName());
        }

        getChildMap().put(childId, nonNullChild);
        nonNullChild.applyMountedID(childId, id != null);
        nonNullChild.init(father, father.getWindow());
        nonNullChild.ensureMountedBranchHasUniqueIDs();
        father.markLayoutDirty();
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T addChildren(@NotNull Widget... children) {
        for (Widget child : children) {
            addChild(child);
        }
        return (T) this;
    }

    default T addChilds(@NotNull Widget... children) {
        return addChildren(children);
    }

    default Widget getChild(@NotNull String id) {
        return getChildMap().get(id);
    }

    @Override
    default void updateChildID(@NotNull Widget child, String oldId, @NotNull String newId) {
        if (oldId != null) {
            getChildMap().remove(oldId);
        }
        getChildMap().put(newId, child);
    }

    @Override
    default void clearChildren() {
        for (Widget child : getChildList()) {
            child.detachFromFather();
        }
        getChildMap().clear();
        ((Widget) this).markLayoutDirty();
    }
}