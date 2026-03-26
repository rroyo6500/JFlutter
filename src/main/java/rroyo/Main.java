package rroyo;

import rroyo.JF.Decorations.Border;
import rroyo.JF.Decorations.BoxShadow;
import rroyo.JF.Decorations.Decoration;
import rroyo.JF.Enums.MainAxisAlignment;
import rroyo.JF.JFComponents.ComplexComponents.JFButton;
import rroyo.JF.JFComponents.ComplexComponents.TestShadow;
import rroyo.JF.JFComponents.SimpleComponents.*;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFWindow window = new JFWindow(800, 800);

        window.addChild(
                new JFCenter(
                    new JFColumn(
                            new JFRow(
                                    new TestShadow(Color.red),
                                    new TestShadow(Color.cyan),
                                    new TestShadow(Color.green),
                                    new TestShadow(Color.yellow)
                            ),
                            new JFRow(
                                    new TestShadow(Color.yellow),
                                    new TestShadow(Color.green),
                                    new TestShadow(Color.cyan),
                                    new TestShadow(Color.red)
                            )
                    )
                )
        );

    }

}
