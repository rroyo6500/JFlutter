package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.Enums.HoverEventTypes;
import rroyo.JF.JFComponents.JFComponent;
import rroyo.JF.JFEvents.JFActionEvent;
import rroyo.JF.JFEvents.JFActionEventSource;
import rroyo.JF.JFEvents.JFHoverEvent;
import rroyo.JF.JFEvents.JFHoverEventSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * JFWindow represents a graphical window component that extends JFComponent.
 * This class manages a JFrame instance and allows customization of its visual
 * appearance and behavior. It also handles drawing and layout management
 * through its associated JPanel.
 * <br>
 * The class provides the ability to set background color, repaint the window,
 * and add a single child component while retaining control over layout and rendering.
 */
public class JFWindow extends JFComponent {

    /**
     * Represents the background color of the `JFWindow` component.
     * This color is used to fill the entire drawing area of the window.
     * The default value is `Color.WHITE`, but it can be updated using the
     * `setColor(Color color)` method for customizing the appearance.
     */
    private Color color = Color.WHITE;

    private JFCanvas canvas;

    /**
     * A `JFrame` instance managed by the `JFWindow` class to represent a graphical window
     * component. This window serves as the main container for rendering graphics, managing
     * layout, and displaying child components.
     * The `window` variable is initialized during the creation of a `JFWindow` instance
     * and is configured with specific parameters such as size, default close operation,
     * and layout settings. It acts as the core element for visual representation in the
     * `JFWindow` class.
     */
    private final JFrame window;

    private JFComponent hoveredComponent;

    /**
     * The `panel` variable represents a custom `JPanel` used as the main content pane
     * for rendering and managing layout operations of the `JFWindow` component.
     * <br>
     * This panel overrides the `paintComponent` method to integrate the custom rendering
     * logic of `JFWindow`. During the paint operation, it executes the following:
     * <br>
     * - Invokes the `layout` method of the enclosing `JFWindow` instance to perform layout
     *   operations for the component and its children. <br>
     * - Validates the structure of the component tree through the `validateTree` method,
     *   ensuring the integrity and proper configuration of all child components. <br>
     * - Calls the `draw` method of the enclosing `JFWindow` instance to manage custom
     *   drawing logic, including the rendering of the current component and its children. <br>
     *
     * Being declared `protected` and `final`, the panel is intended to serve as a core
     * component within the `JFWindow` class while restricting subclass modifications
     * outside of its usage in this context.
     */
    protected final JPanel panel = new JPanel(){
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            JFWindow.this.layout();
            JFWindow.this.validateTree();
            JFWindow.this.draw(g);

            if (canvas != null) canvas.design(g);
        }
    };

    /**
     * Constructs a JFWindow instance with the specified width and height.
     * This constructor initializes a JFrame and configures its size, location,
     * layout, and default behavior. It also adjusts the internal component
     * dimensions to account for window insets.
     *
     * @param width  the width of the JFrame in pixels
     * @param height the height of the JFrame in pixels
     */
    public JFWindow(int width, int height) {
        super();

        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setResizable(false);
        panel.setPreferredSize(new Dimension(width, height));
        window.setContentPane(panel);
        window.pack();
        window.setLocationRelativeTo(null);

        componentBox.setSize(width, height);

        panel.setFocusable(true);
        panel.requestFocusInWindow();
        setupInputListeners();

        window.setVisible(true);
    }

    private void setupInputListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JFComponent target = findTopMostAt(e.getX(), e.getY());
                JFActionEventSource source = findEventSource(target, JFActionEventSource.class);

                if (source != null) {
                    source.dispatchActionEvent(new JFActionEvent((JFComponent) source, "click"));
                }

                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                dispatchHoverTransition(null, e.getX(), e.getY());
                repaint();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                JFComponent target = findTopMostAt(e.getX(), e.getY());
                JFHoverEventSource source = findEventSource(target, JFHoverEventSource.class);
                dispatchHoverTransition((JFComponent) source, e.getX(), e.getY());
                repaint();
            }
        });
    }

    private void dispatchHoverTransition(JFComponent target, int mouseX, int mouseY) {
        if (hoveredComponent != target) {
            if (hoveredComponent instanceof JFHoverEventSource previous) {
                previous.dispatchHoverEvent(new JFHoverEvent(hoveredComponent, mouseX, mouseY, HoverEventTypes.EXIT));
            }

            if (target instanceof JFHoverEventSource next) {
                next.dispatchHoverEvent(new JFHoverEvent(target, mouseX, mouseY, HoverEventTypes.ENTER));
            }
        }

        hoveredComponent = target;

        if (hoveredComponent instanceof JFHoverEventSource current) {
            current.dispatchHoverEvent(new JFHoverEvent(hoveredComponent, mouseX, mouseY, HoverEventTypes.MOVE));
        }
    }

    private <T> T findEventSource(JFComponent start, Class<T> type) {
        JFComponent current = start;

        while (current != null) {
            if (type.isInstance(current)) {
                return type.cast(current);
            }
            current = current.parent;
        }

        return null;
    }

    /**
     * Repaints the current window by invoking the repaint method of the internal panel.
     * This method ensures that the window's visual components are refreshed and any
     * graphical updates are applied.
     */
    public void repaint() {
        panel.repaint();
    }

    /**
     * Sets the color of this JFWindow instance.
     * This method updates the internal color field and returns the current instance
     * to enable method chaining.
     *
     * @param color the Color to be set for this JFWindow. This parameter defines the
     *              visual color of the window.
     * @return the current JFWindow instance with the updated color, facilitating
     *         method chaining for further configurations.
     */
    public JFWindow setColor(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public JFWindow addChild(@NotNull JFComponent child) {
        this.childList.clear();
        hoveredComponent = null;
        super.addChild(child);
        window.repaint();
        return this;
    }

    public JFWindow addCanvas(JFCanvas canvas) {
        this.canvas = canvas;
        return this;
    }

    @Override
    protected void layoutRecalculate() {

    }

    @Override
    protected void design(Graphics g) {
        g.setColor(color);
        g.fillRect(0, 0, componentBox.width, componentBox.height);
    }

}
