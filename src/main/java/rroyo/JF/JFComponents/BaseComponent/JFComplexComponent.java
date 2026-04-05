package rroyo.JF.JFComponents.BaseComponent;

import java.awt.*;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Base class for components that are implemented by composing another component tree internally.
 * <p>
 * A {@code JFComplexComponent} behaves like a thin wrapper around a single root content component.
 * This is useful for building reusable widgets from simpler primitives without forcing callers to
 * care about the internal structure used to render them.
 *
 * @author rroyo
 */
public abstract class JFComplexComponent extends JFComponent implements JFSingleChildComponent<JFComplexComponent> {

    /**
     * Root component representing the internal visual content of this complex component.
     */
    private JFComponent content;

    /**
     * Creates an empty complex component.
     * <p>
     * Subclasses using this constructor are expected to configure their content later.
     */
    public JFComplexComponent() {}

    /**
     * Creates a complex component whose content is produced by the supplied factory.
     *
     * @param contentFactory factory used to build the wrapped internal root component
     */
    protected JFComplexComponent(Supplier<JFComponent> contentFactory) {
        this(contentFactory, true);
    }

    /**
     * Creates a complex component with explicit control over child-first layout behavior.
     *
     * @param contentFactory factory used to build the wrapped internal root component
     * @param layoutRequireChild whether child layout must run before wrapper layout
     */
    protected JFComplexComponent(Supplier<JFComponent> contentFactory, boolean layoutRequireChild) {
        super(layoutRequireChild);
        this.content = Objects.requireNonNull(contentFactory.get(), "contentFactory returned null content");
        attachChild(content);
    }

    /**
     * Returns the internal root content wrapped by this component.
     *
     * @return wrapped content root
     */
    public JFComponent getContent() {
        return content;
    }

    @Override
    public JFComponent setSize(int width, int height) {
        super.setSize(width, height);

        if (content != null) {
            content.setSize(width, height);
        }
        return this;
    }

    /**
     * Adopts the size of the wrapped content and anchors it at local position {@code (0, 0)}.
     * <p>
     * This makes the complex wrapper behave like a transparent shell whose size is entirely
     * dictated by its internal content tree.
     */
    @Override
    protected void layoutRecalculate() {
        if (content == null || !content.isActive()) {
            super.setSize(0, 0);
            return;
        }

        super.setSize(getContent().getWidth(), getContent().getHeight());
        content.setPosition(0, 0);
    }

    /**
     * Complex wrappers do not paint anything directly; rendering is delegated to the content tree.
     *
     * @param g graphics context supplied during the paint pass
     */
    @Override
    protected void design(Graphics g) {
    }

    /**
     * Replaces the wrapped root component exposed by this complex component.
     *
     * @param child new wrapped content root
     * @return current complex component
     */
    @Override
    public JFComplexComponent addChild(JFComponent child) {
        clearChildren();
        this.content = child;
        attachChild(child);
        return this;
    }

    /**
     * Replaces the wrapped content and mounts it as the only child of the wrapper.
     * <p>
     * This method is useful for widgets that need to rebuild their internal composition after
     * a state change while preserving the outer component identity.
     *
     * @param contentFactory factory used to build the new content root
     * @return current complex component
     */
    @Deprecated
    public JFComplexComponent redesignContent(Supplier<JFComponent> contentFactory) {
        return redesign((self, currentContent) ->
                Objects.requireNonNull(contentFactory.get(), "contentFactory returned null content")
        );
    }

    /**
     * Short alias for rebuilding the wrapped content from a supplier.
     *
     * @param contentFactory factory used to build the new content root
     * @return current complex component
     */
    public JFComplexComponent redesign(Supplier<JFComponent> contentFactory) {
        return redesignContent(contentFactory);
    }

    /**
     * Rebuilds the wrapped content from the current content instance and remounts the result.
     * <p>
     * Unlike the supplier-based overload, this variant gives callers access to the existing
     * content tree so they can adapt or wrap it while preserving the internal behavior owned
     * by the complex component itself. This is especially useful for interactive widgets whose
     * logic references nodes inside their original content tree.
     *
     * @param contentTransformer function that receives the current content and returns the new root
     * @return current complex component
     */
    @Deprecated
    public JFComplexComponent redesignContent(Function<JFComponent, JFComponent> contentTransformer) {
        return redesign((self, currentContent) ->
                Objects.requireNonNull(
                        contentTransformer.apply(currentContent),
                        "contentTransformer returned null content"
                )
        );
    }

    /**
     * Short alias for rebuilding the wrapped content from the current content tree.
     *
     * @param contentTransformer function that receives the current content and returns the new root
     * @return current complex component
     */
    public JFComplexComponent redesign(Function<JFComponent, JFComponent> contentTransformer) {
        return redesignContent(contentTransformer);
    }

    /**
     * Rebuilds the wrapped content while exposing both the current component instance and
     * the current content tree to the caller.
     * <p>
     * This overload is the most flexible option for stateful widgets because it lets the
     * transformation lambda call helper methods or inspect fields through {@code self} while
     * still reusing or wrapping the existing content tree. If the transformation or remount
     * fails, the previous content is restored so the component remains usable.
     *
     * @param contentTransformer function receiving {@code self} and the current content root
     * @param <T> concrete complex-component subtype
     * @return current complex component with the concrete subtype preserved
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public <T extends JFComplexComponent> T redesignContent(BiFunction<T, JFComponent, JFComponent> contentTransformer) {
        JFComponent currentContent = content;
        T self = (T) this;

        unmountCurrentContent();
        try {
            JFComponent transformedContent = Objects.requireNonNull(
                    contentTransformer.apply(self, currentContent),
                    "contentTransformer returned null content"
            );
            mountContent(transformedContent);
            return self;
        } catch (RuntimeException | Error ex) {
            restorePreviousContent(currentContent, ex);
            throw ex;
        }
    }

    /**
     * Short alias for rebuilding the wrapped content with access to both the component instance
     * and the current content tree.
     *
     * @param contentTransformer function receiving {@code self} and the current content root
     * @param <T> concrete complex-component subtype
     * @return current complex component with the concrete subtype preserved
     */
    public <T extends JFComplexComponent> T redesign(BiFunction<T, JFComponent, JFComponent> contentTransformer) {
        return redesignContent(contentTransformer);
    }

    /**
     * Detaches the currently mounted content from this wrapper without altering the inner subtree.
     */
    private void unmountCurrentContent() {
        if (content == null) {
            clearChildren();
            return;
        }

        clearChildren();
        content.parent = null;
    }

    /**
     * Mounts the supplied content as the only wrapped child of this complex component.
     *
     * @param newContent content root to mount
     */
    private void mountContent(JFComponent newContent) {
        this.content = newContent;
        attachChild(newContent);
    }

    /**
     * Tries to restore the previous content after a failed redesign attempt.
     *
     * @param previousContent content that was mounted before the redesign
     * @param cause original exception that triggered the rollback
     */
    private void restorePreviousContent(JFComponent previousContent, Throwable cause) {
        if (previousContent == null) {
            content = null;
            return;
        }

        try {
            mountContent(previousContent);
        } catch (RuntimeException | Error restoreError) {
            cause.addSuppressed(restoreError);
        }
    }

}
