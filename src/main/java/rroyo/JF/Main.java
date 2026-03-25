package rroyo.JF;

import rroyo.JF.Decorations.Decoration;
import rroyo.JF.JFComponents.SimpleComponents.*;
import rroyo.JF.JFEvents.JFHoverEvent;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFWindow window = new JFWindow(800, 800);

        window.addChild(
                new JFCenter(
                        new JFContainer(200, 200, new Decoration(Color.RED))
                                .addActionListener(e -> System.out.println("Clicked"))
                                .addHoverListener(e -> {
                                    if (e.getType() == JFHoverEvent.Type.ENTER)
                                        ((JFContainer) e.getSource()).getDecoration().setColor(Color.BLUE);
                                    else if (e.getType() == JFHoverEvent.Type.EXIT)
                                        ((JFContainer) e.getSource()).getDecoration().setColor(Color.RED);
                                    window.repaint();
                                })
                )
        );

    }

}
