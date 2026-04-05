package rroyo.JF.JFComponents.BaseComponent;

import org.jetbrains.annotations.NotNull;

/**
 * Public contract for components that can host multiple children.
 *
 * @param <T> concrete component type returned for fluent composition
 */
public interface JFMultiChildComponent<T extends JFComponent> {

    /**
     * Adds a single child to the component.
     *
     * @param child child to attach
     * @return current component
     */
    T addChild(@NotNull JFComponent child);

    /**
     * Adds one or more children to the component in the provided order.
     *
     * @param children children to attach
     * @return current component
     */
    T addChilds(@NotNull JFComponent... children);
}
