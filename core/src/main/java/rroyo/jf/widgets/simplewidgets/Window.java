package rroyo.jf.widgets.simplewidgets;

import lombok.Getter;
import lombok.Setter;
import rroyo.JUtils.Utils.Logging.LoggerAux;
import rroyo.jf.enums.WindowOperatios;
import rroyo.jf.events.*;
import rroyo.jf.widgets.basewidgets.Widget;
import rroyo.jf.widgets.childcomponent.MultiChildComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Window extends Widget implements MultiChildComponent<Window> {

    @Getter @Setter
    protected Color color = Color.white;

    private final JFrame window;

    private final List<Widget> overlayWidgets = new ArrayList<>();

    private final int frameRate = 15;

    private int loadedChild = 0;

    private final InputManager inputManager;

    // Panel
    protected final JPanel panel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Window.this.draw(g);

            for (Widget widget : overlayWidgets) {
                widget.drawTree(g);
            }

        }
    };

    public Window(Dimension dimension, String title) {
        this(dimension, title, WindowOperatios.EXIT_ON_CLOSE);
    }

    public Window(Dimension dimensions, String title, WindowOperatios defaultOperation) {
        window = new JFrame(title);
        window.setDefaultCloseOperation(defaultOperation.getValue());
        window.setLayout(null);
        window.setResizable(false);

        panel.setPreferredSize(dimensions);
        window.setContentPane(panel);
        window.pack();

        setSize(dimensions);

        panel.setBorder(null);
        panel.setFocusable(true);
        panel.requestFocusInWindow();

        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                Dimension dimension = panel.getSize();

                setSize(dimension);
                panel.setPreferredSize(dimension);

                Window.this.layout();
                Window.this.validateTree();

                repaint();
            }
        });
        window.setLocationRelativeTo(null);

        setWindow(this);

        inputManager = new InputManager(this, panel);
    }

    public Widget findTopMostAtWithOverlays(int x, int y) {
        for (Widget overlayWidget : overlayWidgets) {
            Widget target = overlayWidget.findTopMostAt(x, y);
            if (target != null) {
                return target;
            }
        }

        Widget activeChild = getLoadedChildPanel();
        if (activeChild == null) {
            return containsPoint(x, y) ? this : null;
        }

        Widget target = activeChild.findTopMostAt(x, y);
        return target != null ? target : containsPoint(x, y) ? this : null;
    }

    // ----

    public void close() {
        window.dispose();
    }

    public void repaint() {
        panel.repaint();
    }

    public boolean isVisible() {
        return window.isVisible();
    }

    public Window setVisible(boolean visible) {
        window.setVisible(visible);
        return this;
    }

    public Window setResizable(boolean resizable) {
        window.setResizable(resizable);
        return this;
    }

    private void setupTimer() {
        int delay = 1000 / frameRate;
        Timer timer = new Timer(delay, e -> {
            repaint();
        });
        timer.start();
    }

    public Widget getLoadedChildPanel() {
        List<Widget> children = getChildList();
        if (loadedChild < 0 || loadedChild >= children.size()) {
            return null;
        }
        return children.get(loadedChild);
    }

    public Window loadChildPanel(int childIndex) {
        List<Widget> children = getChildList();
        if (loadedChild < 0 || loadedChild >= children.size()) {
            IndexOutOfBoundsException e =
                    new IndexOutOfBoundsException(
                            "Child index " + childIndex + " is out of bounds for " + children.size() + " root children."
                    );
            LoggerAux.error(e);
            throw e;
        }

        inputManager.changePanel();
        loadedChild = childIndex-1;

        for (Widget child : children) {
            boolean isSelected = (children.indexOf(child) == loadedChild);
            child.setActive(isSelected);
            child.setVisible(isSelected);
        }

        invalidateLayout();
        repaint();

        LoggerAux.info("Loaded ChildPanel " + childIndex);

        return this;
    }

    @Override
    protected void layoutRecalculate() {

    }

    @Override
    protected void design(Graphics g) {
        g.setColor(color);
        g.fillRect(0, 0, bounds.width, bounds.height);
    }

}
