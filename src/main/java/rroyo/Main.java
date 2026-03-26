package rroyo;

import rroyo.CustomComponents.DrawingBoard;
import rroyo.JF.JFComponents.SimpleComponents.*;
import rroyo.JF.JFComponents.ComplexComponents.*;
import rroyo.JF.JFComponents.*;
import rroyo.JF.Decorations.*;
import rroyo.JF.Enums.*;

import java.awt.*;

/**
 * Application entry point that builds and renders a sample component tree.
 *
 * @author rroyo
 */
public class Main {

    /**
     * Launches the example window and mounts the demo layout.
     *
     * @param args command-line arguments passed at startup
     */
    public static void main(String[] args) {

        JFWindow window = new JFWindow(800, 800);

        window.addChild(
                new JFRow(
                        new JFContainer(100, 600, Color.lightGray),
                        new JFSizedBox(600, 600).addChild(
                                new JFStack(Alignment.TOP).addChilds(
                                        new JFContainer(100, 100, Color.RED)
                                )
                        )
                )
        );

    }

}
