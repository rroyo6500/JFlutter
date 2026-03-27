package rroyo;

import rroyo.CustomComponents.ColorButton;
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

        DrawingBoard db = new DrawingBoard(5, Color.white);

        window.addChild(
                new JFColumn(
                        new JFContainer(800, 50, Color.lightGray).addChild(
                                new JFRow(
                                        new ColorButton(db, Color.red),
                                        new ColorButton(db, Color.blue),
                                        new ColorButton(db, Color.cyan),
                                        new ColorButton(db, Color.green),
                                        new ColorButton(db, Color.yellow),
                                        new ColorButton(db, Color.orange),
                                        new ColorButton(db, Color.magenta),
                                        new ColorButton(db, Color.black),
                                        new ColorButton(db, db.getDefaultColor())
                                ).mainAxisAlignment(MainAxisAlignment.CENTER)
                        ),
                        new JFSizedBox(800, 750).addChild(
                                db
                        )
                )
        );

    }

}
