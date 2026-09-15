import rroyo.JUtils.Utils.GUI.CustomFonts;
import rroyo.jf.decorations.Border;
import rroyo.jf.decorations.BoxShadow;
import rroyo.jf.enums.*;
import rroyo.jf.widgets.basewidgets.Widget;
import rroyo.jf.widgets.complexwidgets.Interactuable;
import rroyo.jf.widgets.complexwidgets.TextField;
import rroyo.jf.widgets.simplewidgets.Container;
import rroyo.jf.widgets.simplewidgets.Text;
import rroyo.jf.widgets.simplewidgets.Window;

import java.awt.*;

import static rroyo.jf.generated.Widgets.*;

void main() throws IOException, FontFormatException {

    CustomFonts.addFont("pk", "sandbox/src/main/resources/Pokemon.ttf", 25);

    Window w = new Window(new Dimension(1000, 1000), "Window");

    Text t = new Text("Hola");

    w.addChild(
            Center().addChild(
                    Column(
                            Container(400, 50, Color.cyan)
                                    .addChild(
                                            TextField(400, 50)
                                                    .addKeyListener(e -> {

                                                        if (e.getType().equals(KeyEventTypes.KEY_TYPED)) {
                                                            TextField textField = (TextField) e.getSource();

                                                            System.out.println("Text: " + textField.getText());
                                                            System.out.println("Writed: " + textField.getWritedText().getText());
                                                            System.out.println("Cursor: " + textField.getCursorText().getText());

                                                            t.setText(textField.getText());
                                                        }

                                                    })
                                    ),
                            Container(400, 100, Color.pink)
                                    .addChild(t)
                    )
            )
    );

    w.setColor(Color.WHITE);
    w.setVisible(true);

    System.out.println(w);

}
