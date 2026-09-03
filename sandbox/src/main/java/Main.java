import rroyo.JUtils.Utils.GUI.CustomFonts;
import rroyo.jf.widgets.simplewidgets.Text;
import rroyo.jf.widgets.simplewidgets.Window;
import widgets.TestWidget;

import java.awt.*;

void main() throws IOException, FontFormatException {

    CustomFonts.addFont("pk", "sandbox/src/main/resources/Pokemon.ttf", 25);

    Window w = new Window(new Dimension(1000, 1000), "Window");

    Text t = new Text("Hola");

    w.addChild(
            null
    );

    w.setColor(Color.WHITE);
    w.setVisible(true);

    System.out.println(w);

}
