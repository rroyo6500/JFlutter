package rroyo.jf.widgets.childcomponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rroyo.jf.widgets.basewidgets.Widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface ChildComponent {

    default List<Widget> getChildList() {
        if (this instanceof Widget widget) {
            return List.copyOf(widget.getChildrenMap().values());
        }
        return Collections.emptyList();
    }

    default List<Widget> getChildListInReverseOrder() {
        List<Widget> children = new ArrayList<>(getChildList());
        Collections.reverse(children);
        return children;
    }

    default Widget getChild(int index) {
        return getChildList().get(index);
    }

    default Widget getChild() {
        List<Widget> children = getChildList();
        return children.isEmpty() ? null : children.getFirst();
    }

    default Widget findByID(@NotNull String id) {
        return findByID(id, null);
    }

    default Widget findByID(@NotNull String id, @Nullable Widget ignoredComponent) {
        String searchedId = Widget.validateComponentID(id);

        for (Widget child : getChildList()) {
            if (child != ignoredComponent && searchedId.equals(child.getId())) {
                return child;
            }

            if (child instanceof ChildComponent childComponent) {
                Widget match = childComponent.findByID(searchedId, ignoredComponent);
                if (match != null) return match;
            }
        }

        return null;
    }

    default String getComponentTree() {
        StringBuilder tree = new StringBuilder();
        appendComponentTree((Widget) this, tree, "", true);
        return tree.toString();
    }

    default void printComponentTree() {
        System.out.println(getComponentTree());
    }

    private static void appendComponentTree(Widget component, StringBuilder tree, String prefix, boolean last) {
        tree.append(prefix)
                .append(last ? "`-- " : "|-- ")
                .append(formatTreeNode(component))
                .append(System.lineSeparator());

        if (!(component instanceof ChildComponent childComponent)) {
            return;
        }

        List<Widget> children = childComponent.getChildList();
        for (int i = 0; i < children.size(); i++) {
            appendComponentTree(
                    children.get(i),
                    tree,
                    prefix + (last ? "    " : "|   "),
                    i == children.size() - 1
            );
        }
    }

    private static String formatTreeNode(Widget component) {
        String id = component.getId() == null ? "<no-id>" : component.getId();
        return component.getClass().getSimpleName() + " [id=" + id + "]";
    }

    void updateChildID(@NotNull Widget child, String oldId, @NotNull String newId);

    void clearChildren();
}