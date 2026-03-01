package rroyo.JF;

import rroyo.JF.JFComponents.SimpleComponents.*;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFWindow window = new JFWindow(500, 300);

        window.addChild(
                new JFCenter(
                        new JFContainer().setColor(Color.cyan).setSize(400, 200).addChild(
                                new JFColumn().addChilds(
                                        new JFRow().addChilds(
                                                new JFColumn().addChilds(
                                                        new JFText("Text1"),
                                                        new JFText("Text2")
                                                ).setHeight(100).mainAxisAlingment(JFColumn.MainAxisAlignment.SPACE_EVENLY),
                                                new JFContainer().setColor(Color.red).setSize(25, 25)
                                        ).setWidth(400).mainAxisAlingment(JFRow.MainAxisAlignment.SPACE_AROUND)
                                                .crossAxisAlingment(JFRow.CrossAxisAlignment.CENTER)
                                ).mainAxisAlingment(JFColumn.MainAxisAlignment.CENTER)
                        )
                )
        );

    }

}
