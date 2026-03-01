package rroyo.JF;

import rroyo.JF.JFComponents.ComplexComponents.JFTestComplex;
import rroyo.JF.JFComponents.SimpleComponents.JFColumn;
import rroyo.JF.JFComponents.SimpleComponents.JFContainer;
import rroyo.JF.JFComponents.SimpleComponents.JFRow;
import rroyo.JF.JFComponents.SimpleComponents.JFWindow;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFWindow window = new JFWindow(400, 400);

        window.addChild(
                new JFRow().addChilds(
                        new JFTestComplex(),
                        new JFColumn().addChilds(
                                new JFTestComplex(),
                                new JFRow().addChilds(
                                        new JFTestComplex(),
                                        new JFTestComplex()
                                )
                        )
                )
        );

    }

}
