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
