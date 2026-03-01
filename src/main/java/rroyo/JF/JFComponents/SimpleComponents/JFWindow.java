package rroyo.JF.JFComponents.SimpleComponents;

import org.jetbrains.annotations.NotNull;
import rroyo.JF.JFComponents.JFComponent;

import javax.swing.*;
import java.awt.*;

public class JFWindow extends JFComponent {

    JFrame window = new JFrame();
    JPanel panel = new JPanel(){
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            JFWindow.this.layout();
            JFWindow.this.validateTree();
            JFWindow.this.draw(g);
        }
    };

    public JFWindow(int width, int height) {
        super();

        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setLayout(null);
        window.setResizable(true);
        window.setContentPane(panel);
        window.setVisible(true);

        Dimension exterior = window.getSize();
        Insets insets = window.getInsets();

        int wUtil = exterior.width - insets.left - insets.right;
        int hUtil = exterior.height - insets.top - insets.bottom;

        componentBox.setSize(wUtil, hUtil);

    }

    @Override
    public JFWindow addChild(@NotNull JFComponent child) {
        this.childList.clear();
        super.addChild(child);
        return this;
    }

    @Override
    public void layoutRecalculate() {

    }

    @Override
    public void design(Graphics g) {

    }
}
