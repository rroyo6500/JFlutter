package rroyo.JF;

import rroyo.JF.JFComponents.JFComponent;
import rroyo.JF.JFComponents.SimpleComponents.*;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFWindow window = new JFWindow(800, 800);

        window.addChild(
                new JFCenter(
                        new JFContainer(100, 100, Color.red)
                )
        );

    }

}
