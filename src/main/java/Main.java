import CustomComponents.TicTacToe.TTTBox;
import rroyo.JF.Enums.Alignment;
import rroyo.JF.Enums.MainAxisAlignment;
import rroyo.JF.JFComponents.SimpleComponents.*;

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

    private static char player = 'X';
    private static TTTBox[][] boxes = new TTTBox[3][3];

    /**
     * Boots the demo application and creates the root window.
     * <p>
     * Additional sample components can be mounted here when testing framework changes.
     *
     * @param args command-line arguments passed to the Java process
     */
    public static void main(String[] args) {

        JFWindow window = new JFWindow(800, 800, "").setVisible(true);

        //
        JFSizedBox victoryBox = new JFSizedBox(500, 500)
                .addChild(new JFText("Victory!"));
        victoryBox.setVisible(false);

        JFColumn board = new JFColumn();
        for (int i = 0; i < 3; i++) {
            JFRow row = new JFRow();
            for (int j = 0; j < 3; j++) {
                TTTBox box = new TTTBox(100, 100, "src/main/resources/images/X.png", "src/main/resources/images/O.png");
                box.addActionListener(e -> {
                    if (box.setValue(player)) {
                        if (compVisctory()) victoryBox.setVisible(true);
                        player = (player == 'X') ? 'O' : 'X';
                    }
                });
                boxes[i][j] = box;
                row.addChild(box);
            }
            board.addChild(row);
        }

        window.addChild(
                new JFStack(Alignment.CENTER).addChilds(
                        board.mainAxisAlignment(MainAxisAlignment.CENTER),
                        victoryBox
                )
        );


    }

    private static boolean compVisctory() {

        for (int i = 0; i < 3; i++) {
            if (boxes[i][0].getValue() == player &&
                    boxes[i][1].getValue() == player &&
                    boxes[i][2].getValue() == player
            ) return true;
        }

        for (int i = 0; i < 3; i++) {
            if (boxes[0][i].getValue() == player &&
                    boxes[1][i].getValue() == player &&
                    boxes[2][i].getValue() == player
            ) return true;
        }

        return boxes[0][0].getValue() == player && boxes[1][1].getValue() == player && boxes[2][2].getValue() == player ||
                boxes[0][2].getValue() == player && boxes[1][1].getValue() == player && boxes[2][0].getValue() == player;

    }

}


/*
        DrawingBoard db = new DrawingBoard(5, Color.white);
        JFContainer colorViwer = new JFContainer(500, 50, Color.lightGray);
        JFContainer drawIndicator = new JFContainer(500, 50, Color.lightGray);

        window.addChild(
                new JFColumn(
                        new JFSizedBox(1000, 50).addChild(
                                new JFSizedStack(Alignment.CUSTOM, 1000, 50).addChilds(
                                        new JFRow(
                                                colorViwer,
                                                drawIndicator
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
                Decoration d = drawIndicator.getDecoration();

                if (d.getColor() == Color.lightGray)
                    drawIndicator.getDecoration().setColor(db.getCurrentColor());
                else drawIndicator.getDecoration().setColor(Color.lightGray);

            }
        });
        db.addHoverListener(e -> {
            if (e.getType() == HoverEventTypes.MOVE) {
                Decoration d = drawIndicator.getDecoration();

                if (d.getColor() != Color.lightGray && d.getColor() != db.getCurrentColor())
                    drawIndicator.getDecoration().setColor(db.getCurrentColor());
            }
        });
 */
