package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.Enums.CrossAxisAlignment;
import rroyo.JF.JFComponents.Enums.MainAxisAlignment;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

/**
 * The JFCenter class is a specialized {@link JFComponent} that centers
 * a single child component within its parent container. The child
 * component's position is dynamically recalculated based on its dimensions
 * and the dimensions of the parent container.
 */
public class JFCenter extends JFComponent {

    /**
     * Constructs a new JFCenter component, which centers its child component
     * within its parent container.
     *
     * @param child the child component to be centered within the parent container
     */
    public JFCenter(JFComponent child) {
        super(true);
        addChild(child);
    }

    @Override
    public JFComponent addChild(@NotNull JFComponent child) {
        childList.clear();
        return super.addChild(child);
    }

    @Override
    protected void layoutRecalculate() {

        setSize(parent.componentBox.width, parent.componentBox.height);

        if (childList.isEmpty()) return;

        JFComponent child = childList.getFirst();

        int childX = (componentBox.width / 2) - (child.componentBox.width / 2);
        int childY = (componentBox.height / 2) - (child.componentBox.height / 2);

        child.setPosition(childX, childY);

        if (child.getClass() == JFRow.class || child.getClass() == JFColumn.class) {
            JFFlex flex = (JFFlex) child;

            if (flex.maa == MainAxisAlignment.DEFAULT)
                flex.mainAxisAlignment(MainAxisAlignment.CENTER);
            if (flex.caa == CrossAxisAlignment.DEFAULT)
                flex.crossAxisAlignment(CrossAxisAlignment.CENTER);
        }

    }

    @Override
    protected void design(Graphics g) {

    }

    @Override
    protected void mouseClickAction() {

    }
}
