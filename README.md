# JFlutter

## Widget generation

`WidgetProcessor` discovers widgets by inheritance. A widget is any concrete `public` class extending:

```java
rroyo.jf.widgets.basewidgets.Widget
```

`@JFWidget` is optional. It can configure generation without being required for discovery:

```java
@JFWidget(name = "box")
public class SizedBox extends Widget {
    public SizedBox() {}
    public SizedBox(int width, int height) {}
}
```

This produces overloads such as:

```java
Widgets.box();
Widgets.box(100, 50);
```

The processor creates one factory method for every public constructor. Constructor parameters, types and varargs are preserved.

### Inherited widgets in other projects

The generated `rroyo.jf.generated.Widgets` is also used as the exported widget catalogue. When another module imports JFlutter and runs `WidgetProcessor`, the processor reads the `Widgets` class already available on the classpath and combines those factories with widgets declared by the current module.

This avoids requiring `@JFWidget` on every custom widget and lets `sandbox` and downstream projects add their own widgets.

### Maven and JDK 23+

The project enables annotation processing explicitly with:

```xml
<proc>full</proc>
```

and makes the processor a transitive dependency of `core` so it is available to applications importing JFlutter.

A consumer project using a recent JDK and Maven compiler configuration should explicitly enable annotation processing as well:

```xml
<properties>
    <maven.compiler.proc>full</maven.compiler.proc>
</properties>
```

or configure `maven-compiler-plugin` with `<proc>full</proc>` and/or an explicit processor path.
