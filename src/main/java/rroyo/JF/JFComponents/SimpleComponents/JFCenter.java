package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.Enums.Alignment;
import rroyo.JF.Enums.CrossAxisAlignment;
import rroyo.JF.Enums.MainAxisAlignment;
import rroyo.JF.JFComponents.JFComponent;

import java.awt.*;

/**
 * The JFCenter class is a specialized {@link JFComponent} that centers
 * a single child component within its parent container. The child
 * component's position is dynamically recalculated based on its dimensions
 * and the dimensions of the parent container.
 *
 * @author rroyo
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
        if (childList.isEmpty()) return;
        JFComponent child = childList.getFirst();

        if (parent.getWidth() != 0 && parent.getHeight() != 0)
            setSize(parent.getWidth(), parent.getHeight());
        else
            setSize(child.getWidth(), child.getHeight());

        int[] position = Alignment.CENTER.calculatePosition(componentBox, child.getWidth(), child.getHeight());

        child.setPosition(position[0], position[1]);

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

}
