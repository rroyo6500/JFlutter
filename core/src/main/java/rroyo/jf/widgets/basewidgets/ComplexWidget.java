package rroyo.jf.widgets.basewidgets;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import rroyo.JUtils.Utils.Logging.LoggerAux;
import rroyo.jf.widgets.childcomponent.ChildComponent;
import rroyo.jf.widgets.simplewidgets.Window;

import java.awt.*;

@Getter
public abstract class ComplexWidget extends Widget {

    private Widget content;

    public ComplexWidget() {
        this(false);
    }

    public ComplexWidget(boolean layoutRequireChild) {
        super(layoutRequireChild);

        if (this instanceof ChildComponent) {
            IllegalStateException e = new IllegalStateException(String.format(
                    "[Architecture Error] '%s' cannot implement ChildComponent interfaces.%n" +
                            "  Reason: ComplexWidget composes UI via build() and cannot manage direct children.%n" +
                            "  Fix   : Remove the interface, or extend 'Widget' directly instead.",
                    getClass().getSimpleName()
            ));
            LoggerAux.error(e);
            throw e;
        }

    }

    @Override
    public Widget setSize(int width, int height) {
        super.setSize(width, height);
        if (content != null)
            content.setSize(width, height);
        return this;
    }

    protected final void buildComplexWidget() {
        this.content = build();
        this.content.init(this, window);
    }

    @Override
    public void setWindow(@Nullable Window window) {
        super.setWindow(window);
        if (content != null) content.setWindow(window);
    }

    protected abstract Widget build();

    @Override
    protected final void layoutRecalculate() {
        if (layoutRequiredChild) {
            content.layout();
        }

        if (content == null) {
            buildComplexWidget();
        }

        if (content == null ||!content.isActive()) {
            super.setSize(0, 0);
            return;
        }

        super.setSize(getContent().getSize().width, getContent().getSize().height);
        content.setPosition(0, 0);

        content.layout();
    }

    @Override
    protected final void design(Graphics g) {
        content.draw(g);
    }
}
