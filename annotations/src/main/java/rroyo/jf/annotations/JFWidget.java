package rroyo.jf.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Optional configuration for a JFlutter widget.
 *
 * <p>The annotation is no longer required for a class to be discovered by
 * {@code WidgetProcessor}. Any concrete, accessible class extending
 * {@code rroyo.jf.widgets.basewidgets.Widget} is discovered automatically.</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface JFWidget {

    /**
     * Overrides the factory method name generated in {@code Widgets}.
     * An empty value keeps the widget class simple name.
     */
    String name() default "";

    /**
     * When false, the widget is discovered but no factory methods are
     * generated for it.
     */
    boolean generateFactory() default true;
}
