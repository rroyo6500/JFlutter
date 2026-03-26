package rroyo.JF.JFComponents;

import java.awt.*;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Base component that wraps a single content component and exposes composition helpers.
 *
 * @author rroyo
 */
public abstract class JFComplexComponent extends JFComponent{

    /**
     * Current wrapped content component.
     */
    private JFComponent content;

    /**
     * Creates a complex component using default layout behavior.
     *
     * @param contentFactory supplier used to instantiate wrapped content
     */
    protected JFComplexComponent(Supplier<JFComponent> contentFactory) {
        this(contentFactory, false);
    }

    /**
     * Creates a complex component and configures whether it depends on child layout first.
     *
     * @param contentFactory supplier used to instantiate wrapped content
     * @param layoutRequireChild whether child layout must run before parent layout
     */
    protected JFComplexComponent(Supplier<JFComponent> contentFactory, boolean layoutRequireChild) {
        super(layoutRequireChild);
        this.content = Objects.requireNonNull(contentFactory.get(), "contentFactory returned null content");
        addChild(content);
    }

    /**
     * Returns the wrapped content component.
     *
     * @return current content component
     */
    public JFComponent getContent() {
        return content;
    }

    @Override
    protected void layoutRecalculate() {
        setSize(content.componentBox.width, content.componentBox.height);
        content.setPosition(0, 0);
    }

    @Override
    protected void design(Graphics g) {
    }

    /**
     * Replaces the wrapped content and reattaches it as the only child.
     *
     * @param content new content component to wrap
     * @return current complex component instance
     */
    public JFComplexComponent redesignContent (JFComponent content) {
        this.content = content;
        childList.clear();
        addChild(content);
        return this;
    }

}
