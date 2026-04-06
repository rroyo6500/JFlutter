package rroyo.JF.JFEvents;

import rroyo.JF.JFComponents.BaseComponent.JFComponent;

/**
 * Allows a component subtree to redirect keyboard focus to a specific owner component.
 * <p>
 * This is useful for complex widgets that are composed of several interactive inner nodes
 * but want keyboard input to be handled by a single outer component.
 *
 * @author rroyo
 */
public interface JFFocusTargetComponent {

    /**
     * Returns the component that should own keyboard focus for this subtree.
     *
     * @return keyboard focus target
     */
    JFComponent getKeyFocusTarget();
}
