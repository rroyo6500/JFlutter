package rroyo.JF.JFComponents.ComplexComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.Enums.SelectionType;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;
import rroyo.JF.JFComponents.BaseComponent.JFComplexComponent;
import rroyo.JF.JFComponents.BaseComponent.JFMultiChildComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFColumn;
import rroyo.JF.JFComponents.SimpleComponents.JFSizedBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Component container that groups round selection controls into a mutually exclusive set.
 * <p>
 * The group can be mounted directly in the component tree and automatically manages the
 * selection state of the controls added to it.
 *
 * @author rroyo
 */
public class JFGroup extends JFComplexComponent implements JFMultiChildComponent<JFGroup> {

    private final JFColumn column;
    private final List<JFSelectionControl> members = new ArrayList<>();

    /**
     * Creates an empty group container.
     */
    public JFGroup() {
        this(new JFColumn());
    }

    private JFGroup(JFColumn column) {
        super(() -> column);
        this.column = column;
    }

    @Override
    public JFGroup addChild(@NotNull JFComponent child) {
        if (!(child instanceof JFSelectionControl selector)) {
            throw new IllegalArgumentException("JFGroup only accepts JFSelectionControl children.");
        }

        if (selector.getType() != SelectionType.ROUND) {
            throw new IllegalArgumentException("JFGroup only accepts ROUND selection controls.");
        }

        column.addChilds(
                selector,
                new JFSizedBox(1, 5)
        );
        selector.setGroup(this);
        return this;
    }

    /**
     * Convenience alias for adding a selection control to the group.
     *
     * @param selector selector to add
     * @return current group
     */
    public JFGroup addSelection(@NotNull JFSelectionControl selector) {
        return addChild(selector);
    }

    @Override
    public JFGroup addChilds(@NotNull JFComponent... children) {
        for (JFComponent child : children) {
            addChild(child);
        }
        return this;
    }

    /**
     * Convenience alias for adding multiple selection controls.
     *
     * @param selectors selectors to add
     * @return current group
     */
    public JFGroup addSelections(@NotNull JFSelectionControl... selectors) {
        for (JFSelectionControl selector : selectors) {
            addSelection(selector);
        }
        return this;
    }

    /**
     * Creates, adds and returns a new round selector with the supplied label.
     *
     * @param text label for the new selector
     * @return created selector already associated with this group
     */
    public JFRoundSelector addRoundSelector(String text) {
        JFRoundSelector selector = new JFRoundSelector(text);
        addSelection(selector);
        return selector;
    }

    /**
     * Returns the currently selected control, or {@code null} when none is selected.
     *
     * @return selected control or {@code null}
     */
    public JFSelectionControl getSelected() {
        for (JFSelectionControl member : members) {
            if (member.isSelected()) {
                return member;
            }
        }
        return null;
    }

    /**
     * Selects the given control and unselects the rest of the group.
     *
     * @param selected control that should remain selected
     */
    void select(JFSelectionControl selected) {
        for (JFSelectionControl member : List.copyOf(members)) {
            member.updateSelectedState(member == selected, true);
        }
    }

    /**
     * Unselects all members of the group.
     */
    public void clearSelection() {
        for (JFSelectionControl member : List.copyOf(members)) {
            member.updateSelectedState(false, true);
        }
    }

    /**
     * Registers a member in the group without adding it visually.
     *
     * @param member selector to register
     */
    void attachMember(JFSelectionControl member) {
        if (member != null && !members.contains(member)) {
            members.add(member);
        }
    }

    /**
     * Removes a member from the group registry.
     *
     * @param member selector to unregister
     */
    void detachMember(JFSelectionControl member) {
        members.remove(member);
    }
}
