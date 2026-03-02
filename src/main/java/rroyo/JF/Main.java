package rroyo.JF;

import rroyo.JF.JFComponents.SimpleComponents.*;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFWindow window = new JFWindow(800, 800);

        window.addChild(
                new JFColumn().addChilds(
                        new JFRow().addChilds(
                                new JFText("Hello World")
                        )
                )
        );

    }

}
