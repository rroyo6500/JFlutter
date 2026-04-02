package rroyo;

import rroyo.JF.Enums.*;
import rroyo.JF.Decorations.*;
import rroyo.CustomComponents.*;
import rroyo.JF.JFComponents.SimpleComponents.*;
import rroyo.JF.JFComponents.ComplexComponents.*;

import java.awt.*;

/**
 * Example application entry point used as a manual playground for the framework.
 * <p>
 * The class is intentionally simple and is meant to be edited while testing new layout,
 * decoration and event-system features. It creates a top-level {@link JFWindow} and can be
 * populated with demo components as needed during development.
 *
 * @author rroyo
 */
public class Main {

    /**
     * Boots the demo application and creates the root window.
     * <p>
     * Additional sample components can be mounted here when testing framework changes.
     *
     * @param args command-line arguments passed to the Java process
     */
    public static void main(String[] args) {

        JFWindow window = new JFWindow(1000, 1000);

        //

    }

}


/*
        DrawingBoard db = new DrawingBoard(5, Color.white);
        JFContainer colorViwer = new JFContainer(500, 50, Color.lightGray);
        JFContainer DrawIndicator = new JFContainer(500, 50, Color.lightGray);

        window.addChild(
                new JFColumn(
                        new JFSizedBox(1000, 50).addChild(
                                new JFSizedStack(Alignment.CUSTOM, 1000, 50).addChilds(
                                        new JFRow(
                                                colorViwer,
                                                DrawIndicator
                                        ),
                                        new JFCenter(
                                                new JFRow(
                                                        new ColorButton(colorViwer, db, Color.red),
                                                        new ColorButton(colorViwer, db, Color.blue),
                                                        new ColorButton(colorViwer, db, Color.cyan),
                                                        new ColorButton(colorViwer, db, Color.green),
                                                        new ColorButton(colorViwer, db, Color.yellow),
                                                        new ColorButton(colorViwer, db, Color.orange),
                                                        new ColorButton(colorViwer, db, Color.magenta),
                                                        new ColorButton(colorViwer, db, Color.black),
                                                        new ColorButton(colorViwer, db, db.getDefaultColor())
                                                ).mainAxisAlignment(MainAxisAlignment.CENTER)
                                        )
                                )
                        ),
                        new JFSizedBox(1000, 950).addChild(db)
                )
        );
        db.addActionListener(e -> {
            if (e.getAction() == ActionEventTypes.UP) {
                Decoration d = DrawIndicator.getDecoration();

                if (d.getColor() == Color.lightGray)
                    DrawIndicator.getDecoration().setColor(db.getCurrentColor());
                else DrawIndicator.getDecoration().setColor(Color.lightGray);

            }
        });
        db.addHoverListener(e -> {
            if (e.getType() == HoverEventTypes.MOVE) {
                Decoration d = DrawIndicator.getDecoration();

                if (d.getColor() != Color.lightGray && d.getColor() != db.getCurrentColor())
                    DrawIndicator.getDecoration().setColor(db.getCurrentColor());
            }
        });
 */