package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.Enums.ActionEventTypes;
import rroyo.JF.Enums.HoverEventTypes;
import rroyo.JF.Enums.KeyEventTypes;
import rroyo.JF.JFComponents.BaseComponent.JFComponent;
import rroyo.JF.JFComponents.BaseComponent.JFSingleChildComponent;
import rroyo.JF.JFEvents.JFActionComponent;
import rroyo.JF.JFEvents.JFActionEvent;
import rroyo.JF.JFEvents.JFFocusTargetComponent;
import rroyo.JF.JFEvents.JFHoverComponent;
import rroyo.JF.JFEvents.JFHoverEvent;
import rroyo.JF.JFEvents.JFKeyComponent;
import rroyo.JF.JFEvents.JFKeyEvent;
import rroyo.JF.JFEvents.JFWheelComponent;
import rroyo.JF.JFEvents.JFWheelEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Root component that hosts a framework component tree inside a Swing window.
 * <p>
 * {@code JFWindow} bridges the custom framework with the underlying Swing environment.
 * It owns the actual {@link JFrame}, performs the top-level paint cycle, tracks hover and
 * keyboard focus state, and translates raw Swing input events into framework-specific action,
 * hover and keyboard events.
 * <p>
 * Unlike the rest of the components, a window is always the root of a tree and only accepts
 * a single direct child. Its dimensions define the coordinate space used by all descendants.
 *
 * @author rroyo
 */
public class JFWindow extends JFComponent implements JFSingleChildComponent<JFWindow> {

    /**
     * Background color used to clear the full window surface before drawing children.
     */
    private Color color = Color.WHITE;

    /**
     * Optional post-render canvas used for custom drawing outside the standard component tree.
     */
    private JFCanvas canvas;

    /**
     * Native Swing frame backing this window.
     * <p>
     * The frame is responsible for the actual operating-system window, while the framework
     * keeps ownership of rendering and input dispatch through the content panel.
     */
    private final JFrame window;

    /**
     * Component currently considered to be under the mouse pointer.
     * <p>
     * This cached reference allows the window to emit hover enter and exit transitions only
     * when the pointer target truly changes.
     */
    private JFComponent hoveredComponent;

    /**
     * Component that currently owns keyboard focus inside the framework.
     * <p>
     * The value is updated whenever the user presses the mouse over a component implementing
     * {@link JFKeyComponent}. Subsequent keyboard input is routed here until focus changes.
     */
    private JFComponent focusedComponent;

    /**
     * Components that should be painted and hit-tested above the regular tree.
     */
    private final List<JFComponent> overlayComponents = new ArrayList<>();

    /**
     * Custom Swing panel used as the frame content pane.
     * <p>
     * The panel delegates the entire visual lifecycle back to the framework. During every paint
     * call it forces a lazy layout pass, validates the resulting tree, draws the component tree,
     * and finally lets an optional extra canvas paint over the result.
     */
    protected final JPanel panel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            JFWindow.this.layout();
            JFWindow.this.validateTree();
            JFWindow.this.draw(g);

            for (JFComponent overlayComponent : overlayComponents) {
                overlayComponent.drawTree(g);
            }

            if (canvas != null) canvas.draw(g);
        }
    };


    /**
     * Creates a top-level window with the default close behavior.
     *
     * @param width width of the window content area in pixels
     * @param height height of the window content area in pixels
     * @param title native window title
     */
    public JFWindow(int width, int height, String title) {
        this(width, height, title, JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Creates a top-level window with a fixed content size.
     * <p>
     * The constructor configures the underlying Swing frame, installs the framework event
     * bridges, makes the panel focusable so it can receive keyboard input, and finally shows
     * the window on screen.
     *
     * @param width width of the window content area in pixels
     * @param height height of the window content area in pixels
     * @param title native window title
     * @param defaultOperation Swing close operation used by the frame
     */
    public JFWindow(int width, int height, String title, int defaultOperation) {
        window = new JFrame();
        window.setDefaultCloseOperation(defaultOperation);
        window.setTitle(title);
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
    }

    /**
     * Installs the Swing listeners that translate raw input into framework events.
     * <p>
     * Mouse events are converted into action events and also drive focus selection. Pointer
     * movement is converted into hover lifecycle events. Keyboard callbacks are routed to the
     * component currently stored in {@link #focusedComponent}.
     */
    private void setupInputListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                panel.requestFocusInWindow();
                JFComponent target = findTopMostAtWithOverlays(e.getX(), e.getY());
                JFActionComponent source = findEventSource(target, JFActionComponent.class);

                if (source != null) {
                    source.dispatchActionEvent(
                            new JFActionEvent((JFComponent) source, ActionEventTypes.UP.setButton(e.getButton()), e.getX(), e.getY())
                    );
                }

                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                panel.requestFocusInWindow();
                JFComponent target = findTopMostAtWithOverlays(e.getX(), e.getY());
                focusedComponent = resolveFocusedComponent(target);
                JFActionComponent source = findEventSource(target, JFActionComponent.class);

                if (source != null) {
                    source.dispatchActionEvent(
                            new JFActionEvent((JFComponent) source, ActionEventTypes.DOWN.setButton(e.getButton()), e.getX(), e.getY())
                    );
                }

                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                panel.requestFocusInWindow();
                JFComponent target = findTopMostAtWithOverlays(e.getX(), e.getY());
                JFActionComponent source = findEventSource(target, JFActionComponent.class);

                if (source != null) {
                    source.dispatchActionEvent(
                            new JFActionEvent((JFComponent) source, ActionEventTypes.CLICK.setButton(e.getButton()), e.getX(), e.getY())
                    );
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
                JFComponent target = findTopMostAtWithOverlays(e.getX(), e.getY());
                JFHoverComponent source = findEventSource(target, JFHoverComponent.class);
                dispatchHoverTransition((JFComponent) source, e.getX(), e.getY());
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                JFComponent target = findTopMostAtWithOverlays(e.getX(), e.getY());
                JFHoverComponent source = findEventSource(target, JFHoverComponent.class);
                dispatchHoverTransition((JFComponent) source, e.getX(), e.getY());
                repaint();
            }
        });

        panel.addMouseWheelListener(e -> {
            JFComponent target = findTopMostAtWithOverlays(e.getX(), e.getY());
            dispatchWheelEvent(target, e);
            repaint();
        });

        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                dispatchKeyEvent(KeyEventTypes.KEY_TYPED, e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                dispatchKeyEvent(KeyEventTypes.KEY_PRESSED, e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                dispatchKeyEvent(KeyEventTypes.KEY_RELEASED, e);
            }
        });
    }

    /**
     * Dispatches a framework keyboard event to the component that currently owns focus.
     * <p>
     * If no focusable component is selected, the event is silently ignored. When a target is
     * present, the raw AWT event is wrapped into {@link JFKeyEvent} and delivered through the
     * framework keyboard event API.
     *
     * @param type framework-level keyboard event category
     * @param e raw AWT keyboard event
     */
    private void dispatchKeyEvent(KeyEventTypes type, KeyEvent e) {
        if (!(focusedComponent instanceof JFKeyComponent keyComponent)) {
            return;
        }

        keyComponent.dispatchKeyEvent(new JFKeyEvent(focusedComponent, type, e.getKeyCode(), e.getKeyChar(), e));
        repaint();
    }

    /**
     * Updates hover state and emits enter, move and exit events when needed.
     * <p>
     * The method compares the last hovered component with the new pointer target. If the target
     * changed, it first emits an exit event for the previous component and an enter event for the
     * new one. It then stores the new target and emits a move event for the current hovered
     * component when appropriate.
     *
     * @param target component currently under the pointer, or {@code null} when outside the window
     * @param mouseX pointer X coordinate in panel space
     * @param mouseY pointer Y coordinate in panel space
     */
    private void dispatchHoverTransition(JFComponent target, int mouseX, int mouseY) {
        if (hoveredComponent != target) {
            if (hoveredComponent instanceof JFHoverComponent previous) {
                previous.dispatchHoverEvent(new JFHoverEvent(hoveredComponent, mouseX, mouseY, HoverEventTypes.EXIT));
            }

            if (target instanceof JFHoverComponent next) {
                next.dispatchHoverEvent(new JFHoverEvent(target, mouseX, mouseY, HoverEventTypes.ENTER));
            }
        }

        hoveredComponent = target;

        if (hoveredComponent instanceof JFHoverComponent current) {
            current.dispatchHoverEvent(new JFHoverEvent(hoveredComponent, mouseX, mouseY, HoverEventTypes.MOVE));
        }
    }

    /**
     * Resolves the topmost component under the pointer, considering overlay components first.
     *
     * @param x pointer x-coordinate
     * @param y pointer y-coordinate
     * @return deepest component found at the given position, or {@code null} when empty
     */
    private JFComponent findTopMostAtWithOverlays(int x, int y) {
        for (int i = overlayComponents.size() - 1; i >= 0; i--) {
            JFComponent overlayComponent = overlayComponents.get(i);
            JFComponent target = overlayComponent.findTopMostAt(x, y);
            if (target != null) {
                return target;
            }
        }

        return findTopMostAt(x, y);
    }

    /**
     * Walks up the component ancestry chain to find the first node implementing a given event role.
     * <p>
     * This allows clicks and hover detection to start from the deepest visual node under the
     * pointer and then bubble up until an interactive ancestor capable of handling the event is found.
     *
     * @param start deepest component initially hit by the pointer
     * @param type event-role interface expected from the returned object
     * @param <T> interface type to resolve
     * @return nearest matching ancestor, or {@code null} when none implements the requested role
     */
    private <T> T findEventSource(JFComponent start, Class<T> type) {
        JFComponent current = start;

        while (current != null) {
            if (type.isInstance(current)) {
                return type.cast(current);
            }
            current = current.getParent();
        }

        return null;
    }

    /**
     * Resolves which component should receive keyboard focus after a pointer press.
     * <p>
     * Complex widgets may expose many nested interactive parts but still want keyboard input to be
     * routed to one outer owner. Components implementing {@link JFFocusTargetComponent} can
     * explicitly declare that owner and take precedence over the default nearest-key-component rule.
     *
     * @param start deepest component initially hit by the pointer
     * @return component that should own keyboard focus, or {@code null} when none is suitable
     */
    private JFComponent resolveFocusedComponent(JFComponent start) {
        JFComponent current = start;
        JFComponent fallbackKeyComponent = null;

        while (current != null) {
            if (current instanceof JFFocusTargetComponent focusTargetComponent) {
                JFComponent focusTarget = focusTargetComponent.getKeyFocusTarget();
                if (focusTarget instanceof JFKeyComponent) {
                    return focusTarget;
                }
            }

            if (fallbackKeyComponent == null && current instanceof JFKeyComponent) {
                fallbackKeyComponent = current;
            }

            current = current.getParent();
        }

        return fallbackKeyComponent;
    }

    /**
     * Dispatches a wheel event through the component ancestry starting at the deepest hit node.
     * <p>
     * Unlike click or hover resolution, wheel input is often expected to bubble through nested
     * interactive wrappers until a scrollable ancestor handles it, so every ancestor implementing
     * {@link JFWheelComponent} receives the event.
     *
     * @param start deepest component found under the pointer
     * @param event raw AWT wheel event
     */
    private void dispatchWheelEvent(JFComponent start, MouseWheelEvent event) {
        JFComponent current = start;

        while (current != null) {
            if (current instanceof JFWheelComponent wheelComponent) {
                wheelComponent.dispatchWheelEvent(new JFWheelEvent(
                        current,
                        event.getX(),
                        event.getY(),
                        event.getWheelRotation(),
                        event.getPreciseWheelRotation(),
                        event
                ));
            }
            current = current.getParent();
        }
    }

    /**
     * Requests a redraw of the internal panel.
     * <p>
     * Swing will schedule a new paint cycle, during which layout, validation and rendering of
     * the full component tree will happen again.
     */
    public void repaint() {
        panel.repaint();
    }

    /**
     * Changes the solid background color used by the window.
     *
     * @param color new background color
     * @return current window for fluent configuration
     */
    public JFWindow setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Replaces the current root child of the window.
     * <p>
     * A window behaves as a single-child root container, so any previous child is removed before
     * the new child is attached. Hover and focus caches are also cleared because they are no longer
     * valid for the old tree.
     *
     * @param child new root component to mount inside the window
     * @return current window for fluent composition
     */
    @Override
    public JFWindow addChild(@NotNull JFComponent child) {
        clearChildren();
        hoveredComponent = null;
        focusedComponent = null;
        overlayComponents.clear();
        attachChild(child);
        window.repaint();
        return this;
    }

    /**
     * Registers a component so it is painted above the regular component tree.
     *
     * @param component overlay root to register
     * @return current window
     */
    public JFWindow registerOverlay(JFComponent component) {
        if (component != null && !overlayComponents.contains(component)) {
            overlayComponents.add(component);
        }
        return this;
    }

    /**
     * Removes a component from the overlay painting layer.
     *
     * @param component overlay root to remove
     * @return current window
     */
    public JFWindow unregisterOverlay(JFComponent component) {
        overlayComponents.remove(component);
        return this;
    }

    /**
     * Registers an extra custom drawing layer rendered after the regular component tree.
     *
     * @param canvas canvas callback rendered on top of the component tree
     * @return current window for fluent configuration
     */
    public JFWindow addCanvas(JFCanvas canvas) {
        this.canvas = canvas;
        return this;
    }



    /**
     * Returns the visibility of the native Swing frame.
     *
     * @return {@code true} when the operating-system window is visible
     */
    @Override
    public boolean isVisible() {
        return window.isVisible();
    }

    /**
     * Shows or hides the native Swing frame.
     *
     * @param visible new frame visibility
     * @return current window
     */
    @Override
    public JFWindow setVisible(boolean visible) {
        window.setVisible(visible);
        return this;
    }

    @Override
    public JFWindow setActive(boolean active) {
        setActive(false);
        return this.setVisible(active);
    }

    /**
     * Window size is fixed at construction time, so no additional layout recalculation is needed.
     */
    @Override
    protected void layoutRecalculate() {

    }

    /**
     * Paints the window background before any children are rendered.
     *
     * @param g graphics context provided by Swing
     */
    @Override
    protected void design(Graphics g) {
        g.setColor(color);
        g.fillRect(0, 0, componentBox.width, componentBox.height);
    }

}
