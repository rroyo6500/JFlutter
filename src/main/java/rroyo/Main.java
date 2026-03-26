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
                        new JFColumn(
                                new JFSizedStack(Alignment.CUSTOM, 400, 400,
                                        new JFSizedStack(Alignment.RIGHT, 400, 400,
                                                new JFContainer(100, 100, Color.red)
                                        ),
                                        new JFSizedStack(Alignment.BOTTOM, 400, 400,
                                                new JFContainer(100, 100, Color.red)
                                        ),
                                        new JFSizedStack(Alignment.CENTER, 400, 400,
                                                new JFContainer(100, 100, Color.red)
                                        )
                                ),
                                new JFSizedStack(Alignment.CUSTOM, 400, 400,
                                        new JFSizedStack(Alignment.RIGHT, 400, 400,
                                                new JFContainer(100, 100, Color.cyan)
                                        ),
                                        new JFSizedStack(Alignment.TOP, 400, 400,
                                                new JFContainer(100, 100, Color.cyan)
                                        ),
                                        new JFSizedStack(Alignment.CENTER, 400, 400,
                                                new JFContainer(100, 100, Color.cyan)
                                        )
                                )
                        ),
                        new JFColumn(
                                new JFSizedStack(Alignment.CUSTOM, 400, 400,
                                        new JFSizedStack(Alignment.LEFT, 400, 400,
                                                new JFContainer(100, 100, Color.green)
                                        ),
                                        new JFSizedStack(Alignment.BOTTOM, 400, 400,
                                                new JFContainer(100, 100, Color.green)
                                        ),
                                        new JFSizedStack(Alignment.CENTER, 400, 400,
                                                new JFContainer(100, 100, Color.green)
                                        )
                                ),
                                new JFSizedStack(Alignment.CUSTOM, 400, 400,
                                        new JFSizedStack(Alignment.LEFT, 400, 400,
                                                new JFContainer(100, 100, Color.orange)
                                        ),
                                        new JFSizedStack(Alignment.TOP, 400, 400,
                                                new JFContainer(100, 100, Color.orange)
                                        ),
                                        new JFSizedStack(Alignment.CENTER, 400, 400,
                                                new JFContainer(100, 100, Color.orange)
                                        )
                                )
                        )
                )

        ).addCanvas(new JFCanvas() {
            @Override
            protected void draw(Graphics g) {
                //g.setColor(Color.MAGENTA);
                //g.fillRect(window.getWidth()/2, 0, 2, window.getHeight());
                //g.fillRect(0, window.getHeight()/2, window.getWidth(), 2);
            }
        });

    }

}
