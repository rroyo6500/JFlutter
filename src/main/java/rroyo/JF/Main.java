package rroyo.JF;

import rroyo.JF.JFComponents.Enums.CrossAxisAlignment;
import rroyo.JF.JFComponents.Enums.MainAxisAlignment;
import rroyo.JF.JFComponents.Enums.Sizes;
import rroyo.JF.JFComponents.JFComponent;
import rroyo.JF.JFComponents.SimpleComponents.*;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFWindow window = new JFWindow(1000, 1000);

        window.addChild(
                new JFCenter(
                        new JFImage("C:\\Users\\r.royo\\Documents\\DAM\\Clase\\Ingles\\Proyectos\\parte2\\auth_aplicacion\\auth_application\\lib\\images\\background1.jpg")
                                .setSizePercentage(50)
                )
        );

    }

}
