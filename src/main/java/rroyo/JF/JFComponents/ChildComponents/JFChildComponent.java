package rroyo.JF.JFComponents.ChildComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base contract shared by every component that owns direct children.
 */
public interface JFChildComponent {

    /**
     * Returns direct children in traversal order.
     *
     * @return direct children
     */
    List<JFComponent> getChildList();

    /**
     * Returns direct children in reverse traversal order.
     *
     * @return direct children from topmost to bottommost
     */
    default List<JFComponent> getChildListInReverseOrder() {
        List<JFComponent> children = new ArrayList<>(getChildList());
        Collections.reverse(children);
        return children;
    }

    /**
     * Returns a child by index.
     *
     * @param index zero-based child index
     * @return child at the specified position
     */
    default JFComponent getChild(int index) {
        return getChildList().get(index);
    }

    /**
     * Returns the first child.
     *
     * @return first child, or {@code null} when empty
     */
    default JFComponent getChild() {
        List<JFComponent> children = getChildList();
        return children.isEmpty() ? null : children.getFirst();
    }

    /**
     * Finds a descendant component by ID.
     *
     * @param id component ID to search
     * @return matching component, or {@code null} when missing
     */
    default JFComponent findByID(@NotNull String id) {
        String searchedId = JFComponent.validateComponentID(id);

        for (JFComponent child : getChildList()) {
            if (searchedId.equals(child.getID())) {
                return child;
            }

            if (child instanceof JFChildComponent childComponent) {
                JFComponent match = childComponent.findByID(searchedId);
                if (match != null) return match;
            }
        }

        return null;
    }

    /**
     * Finds a descendant component by ID while ignoring one component.
     *
     * @param id component ID to search
     * @param ignoredComponent component that should not count as a match
     * @return matching component, or {@code null} when missing
     */
    default JFComponent findByID(@NotNull String id, JFComponent ignoredComponent) {
        String searchedId = JFComponent.validateComponentID(id);

        for (JFComponent child : getChildList()) {
            if (child != ignoredComponent && searchedId.equals(child.getID())) {
                return child;
            }

            if (child instanceof JFChildComponent childComponent) {
                JFComponent match = childComponent.findByID(searchedId, ignoredComponent);
                if (match != null) return match;
            }
        }

        return null;
    }

    /**
     * Returns a formatted text representation of this component tree.
     *
     * @return component tree with class names and IDs
     */
    default String getComponentTree() {
        StringBuilder tree = new StringBuilder();
        appendComponentTree((JFComponent) this, tree, "", true);
        return tree.toString();
    }

    /**
     * Prints this component tree to the standard output.
     */
    default void printComponentTree() {
        System.out.println(getComponentTree());
    }

    private static void appendComponentTree(JFComponent component, StringBuilder tree, String prefix, boolean last) {
        tree.append(prefix)
                .append(last ? "`-- " : "|-- ")
                .append(formatTreeNode(component))
                .append(System.lineSeparator());

        if (!(component instanceof JFChildComponent childComponent)) {
            return;
        }

        List<JFComponent> children = childComponent.getChildList();
        for (int i = 0; i < children.size(); i++) {
            appendComponentTree(
                    children.get(i),
                    tree,
                    prefix + (last ? "    " : "|   "),
                    i == children.size() - 1
            );
        }
    }

    private static String formatTreeNode(JFComponent component) {
        String id = component.getID() == null ? "<no-id>" : component.getID();
        return component.getClass().getSimpleName() + " [id=" + id + "]";
    }

    /**
     * Updates the ID used to store one direct child.
     *
     * @param child child whose ID changed
     * @param oldId previous ID
     * @param newId replacement ID
     */
    void updateChildID(@NotNull JFComponent child, String oldId, @NotNull String newId);

    /**
     * Removes every direct child from this component.
     */
    void clearChildren();
}
