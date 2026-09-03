package rroyo.jf.widgets.childcomponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rroyo.jf.widgets.basewidgets.Widget;

import java.util.Map;
import java.util.Objects;

public interface SingleChildComponent<T extends Widget> extends ChildComponent {

    default T addChild(@NotNull Widget child) {
        return addChild(null, child);
    }

    @SuppressWarnings("unchecked")
    default T addChild(@Nullable String id, @NotNull Widget child) {
        Widget father = (Widget) this;

        clearChildren(); // Desvincula el hijo actual si ya existía uno

        Widget nonNullChild = Objects.requireNonNull(child, "Cannot add a null child component");
        father.validateMountableComponent(nonNullChild);

        String childId = father.resolveMountedComponentID(id, nonNullChild);
        father.getChildrenMap().put(childId, nonNullChild);

        nonNullChild.applyMountedID(childId, id != null);
        nonNullChild.init(father, father.getWindow());
        nonNullChild.ensureMountedBranchHasUniqueIDs();
        father.markLayoutDirty();

        return (T) this;
    }

    @Override
    default Widget getChild() {
        return ChildComponent.super.getChild();
    }

    default String getChildID() {
        Widget child = getChild();
        return child != null ? child.getId() : null;
    }

    @Override
    default void updateChildID(@NotNull Widget child, String oldId, @NotNull String newId) {
        Map<String, Widget> map = ((Widget) this).getChildrenMap();
        if (oldId != null) {
            map.remove(oldId);
        }
        map.put(newId, child);
    }

    @Override
    default void clearChildren() {
        Widget parent = (Widget) this;
        Widget child = getChild();
        if (child != null) {
            child.detachFromFather();
        }
        parent.getChildrenMap().clear();
        parent.markLayoutDirty();
    }
}