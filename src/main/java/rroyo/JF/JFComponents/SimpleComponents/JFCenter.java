package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.Enums.Alignment;
import rroyo.JF.Enums.CrossAxisAlignment;
import rroyo.JF.Enums.MainAxisAlignment;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;
import rroyo.JF.JFComponents.BaseComponent.JFSingleChildComponent;

import java.awt.*;

/**
 * Single-child container that centers its content inside the available parent area.
 * <p>
 * The component is frequently used as a convenience wrapper around text, images, or entire
 * flex layouts that should appear centered without the caller manually calculating coordinates.
 *
 * @author rroyo
 */
public class JFCenter extends JFComponent implements JFSingleChildComponent<JFCenter> {

    /**
     * Creates a centering container for a single child.
     *
     * @param child child component to keep centered
     */
    public JFCenter(JFComponent child) {
        super(true);
        addChild(child);
    }

    /**
     * Replaces any previous child so the center container always owns exactly one child.
     *
     * @param child new centered child
     * @return current component
     */
    @Override
    public JFCenter addChild(@NotNull JFComponent child) {
        clearChildren();
        attachChild(child);
        return this;
    }

    /**
     * Adopts the parent size when available and computes the centered position of the child.
     * <p>
     * When the centered child is itself a row or column and no explicit flex alignment has been
     * configured, this method also applies centered defaults so the child layout feels naturally
     * centered as a whole.
     */
    @Override
    protected void layoutRecalculate() {
        if (childList.isEmpty()) return;
        JFComponent child = childList.getFirst();

        if (!child.isActive()) {
            if (parent != null && parent.getWidth() != 0 && parent.getHeight() != 0)
                setSize(parent.getWidth(), parent.getHeight());
            else
                setSize(0, 0);
            return;
        }

        if (parent.getWidth() != 0 && parent.getHeight() != 0)
            setSize(parent.getWidth(), parent.getHeight());
        else
            setSize(child.getWidth(), child.getHeight());

        Point position = Alignment.CENTER.calculatePosition(componentBox, child.getWidth(), child.getHeight());

        child.setPosition(position.x, position.y);

        if (child.getClass() == JFRow.class || child.getClass() == JFColumn.class) {
            JFFlex flex = (JFFlex) child;

            if (flex.maa == MainAxisAlignment.DEFAULT)
                flex.mainAxisAlignment(MainAxisAlignment.CENTER);
            if (flex.caa == CrossAxisAlignment.DEFAULT)
                flex.crossAxisAlignment(CrossAxisAlignment.CENTER);
        }

    }

    /**
     * The centering helper does not paint any pixels itself.
     *
     * @param g graphics context supplied during the paint pass
     */
    @Override
    protected void design(Graphics g) {

    }

}
