package rroyo.jf.events;

import rroyo.jf.enums.ActionEventTypes;
import rroyo.jf.enums.HoverEventTypes;
import rroyo.jf.enums.KeyEventTypes;
import rroyo.jf.events.Action.ActionEvent;
import rroyo.jf.events.Hover.HoverEvent;
import rroyo.jf.events.Wheel.WheelEvent;
import rroyo.jf.widgets.basewidgets.Widget;
import rroyo.jf.widgets.simplewidgets.Window;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;

public class InputManager {

    private final Window window;
    private final JPanel panel;

    private Widget hoveredWidget;
    private Widget focusedWidget;

    private Point lastMousePosition = new Point(-1, -1);

    public InputManager(Window window, JPanel panel) {
        this.window = window;
        this.panel = panel;
        setupListeners();
    }

    private void setupListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                panel.requestFocusInWindow();
                Widget target = window.findTopMostAtWithOverlays(e.getX(), e.getY());
                ActionWidget source = findEventSource(target, ActionWidget.class);

                if (source != null) {
                    source.dispatchActionEvent(new ActionEvent((Widget) source, ActionEventTypes.UP, e.getButton(), e.getX(), e.getY()));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                panel.requestFocusInWindow();
                Widget target = window.findTopMostAtWithOverlays(e.getX(), e.getY());
                focusedWidget = resolveFocusedComponent(target);
                ActionWidget source = findEventSource(focusedWidget, ActionWidget.class);

                if (source != null) {
                    source.dispatchActionEvent(new ActionEvent((Widget) source, ActionEventTypes.DOWN, e.getButton(), e.getX(), e.getY()));
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                panel.requestFocusInWindow();
                Widget target = window.findTopMostAtWithOverlays(e.getX(), e.getY());
                ActionWidget source = findEventSource(target, ActionWidget.class);

                if (source != null) {
                    source.dispatchActionEvent(new ActionEvent((Widget) source, ActionEventTypes.CLICK, e.getButton(), e.getX(), e.getY()));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                dispatchHoverTransition(null, e.getX(), e.getY());
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (e.getX() == lastMousePosition.getX() && e.getY() == lastMousePosition.getY()) return;
                lastMousePosition.setLocation(e.getX(), e.getY());

                Widget target = window.findTopMostAtWithOverlays(e.getX(), e.getY());
                HoverWidget source = findEventSource(target, HoverWidget.class);
                dispatchHoverTransition((Widget) source, e.getX(), e.getY());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.getX() == lastMousePosition.getX() && e.getY() == lastMousePosition.getY()) return;
                lastMousePosition.setLocation(e.getX(), e.getY());

                Widget target = window.findTopMostAtWithOverlays(e.getX(), e.getY());
                HoverWidget source = findEventSource(target, HoverWidget.class);
                dispatchHoverTransition((Widget) source, e.getX(), e.getY());
            }
        });

        panel.addMouseWheelListener(e -> {
            Widget target = window.findTopMostAtWithOverlays(e.getX(), e.getY());
            dispatchWheelEvent(target, e);
        });

        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                dispatchKeyEvent(KeyEventTypes.KEY_TYPED, e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                dispatchKeyEvent(KeyEventTypes.KEY_PRESSED, e);
                window.repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                dispatchKeyEvent(KeyEventTypes.KEY_RELEASED, e);
            }
        });
    }

    private void dispatchKeyEvent(KeyEventTypes type, KeyEvent e) {
        if (!(focusedWidget instanceof KeyWidget keyComponent)) return;

        keyComponent.dispatchKeyEvent(new rroyo.jf.events.Key.KeyEvent(focusedWidget, type, e.getKeyCode(), e.getKeyChar(), e));
        window.repaint();
    }

    private void dispatchHoverTransition(Widget target, int mouseX, int mouseY) {
        if (hoveredWidget != target) {
            if (hoveredWidget instanceof HoverWidget previous) {
                previous.dispatchHoverEvent(new HoverEvent(hoveredWidget, mouseX, mouseY, HoverEventTypes.EXIT));
            }

            if (target instanceof HoverWidget next) {
                next.dispatchHoverEvent(new HoverEvent(target, mouseX, mouseY, HoverEventTypes.ENTER));
            }
        }

        hoveredWidget = target;

        if (hoveredWidget instanceof HoverWidget current) {
            current.dispatchHoverEvent(new HoverEvent(hoveredWidget, mouseX, mouseY, HoverEventTypes.MOVE));
        }
    }

    private void dispatchWheelEvent(Widget start, MouseWheelEvent event) {
        Widget current = start;
        while (current != null) {
            if (current instanceof WheelWidget wheelWidget) {
                wheelWidget.dispatchWheelEvent(new WheelEvent(
                        current, event.getX(), event.getY(),
                        event.getWheelRotation(), event.getPreciseWheelRotation(), event
                ));
            }
            current = current.getFather();
        }
    }

    private <T> T findEventSource(Widget start, Class<T> type) {
        Widget current = start;
        while (current != null) {
            if (type.isInstance(current)) {
                return type.cast(current);
            }
            current = current.getFather();
        }
        return null;
    }

    private Widget resolveFocusedComponent(Widget start) {
        Widget current = start;
        Widget fallbackKeyComponent = null;

        while (current != null) {
            if (current instanceof FocusTargetWidget focusTargetWidget) {
                Widget focusTarget = focusTargetWidget.getKeyFocusTarget();
                if (focusTarget instanceof KeyWidget) {
                    return focusTarget;
                }
            }
            if (fallbackKeyComponent == null && current instanceof KeyWidget) {
                fallbackKeyComponent = current;
            }
            current = current.getFather();
        }
        return fallbackKeyComponent;
    }

    public void changePanel() {
        hoveredWidget = null;
        focusedWidget = null;
    }

}