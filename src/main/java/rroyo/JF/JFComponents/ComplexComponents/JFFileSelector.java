package rroyo.JF.JFComponents.ComplexComponents;

import rroyo.JF.Decorations.Border;
import rroyo.JF.Decorations.Decoration;
import rroyo.JF.Enums.ActionEventTypes;
import rroyo.JF.Enums.MouseButtons;
import rroyo.JF.JFComponents.BaseComponent.JFComplexComponent;
import rroyo.JF.JFComponents.SimpleComponents.JFCenter;
import rroyo.JF.JFComponents.SimpleComponents.JFContainer;
import rroyo.JF.JFComponents.SimpleComponents.JFText;
import rroyo.JF.JFComponents.SimpleComponents.JFWindow;
import rroyo.JF.JFEvents.JFInteractiveComponent;

import java.awt.*;
import java.io.File;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class JFFileSelector extends JFComplexComponent implements JFInteractiveComponent {

    private JFText textComponent;
    private File selectedFile;

    public JFFileSelector(int width, int height, String text) {
        this(width, height, new JFText(text));
    }

    private JFFileSelector(int width, int height, JFText text) {
        super(() ->
                new JFContainer(width, height,
                        new Decoration(Color.lightGray)
                                .setBorder(new Border(Color.blue, 1))
                ).addChild(
                    new JFCenter(text)
                )
        );
        this.textComponent = text;
        setActionListener();
    }

    private void setActionListener() {
        addActionListener(e -> {

            if (e.getAction().equals(ActionEventTypes.CLICK) && e.getAction().getButton().equals(MouseButtons.LEFT)) {
                FileDialog fd = new FileDialog((Frame) null, "Select a file...", FileDialog.LOAD);
                fd.setSize(800, 400);
                fd.setVisible(true);

                if (fd.getFile() != null) {
                    selectedFile = new File(fd.getDirectory(), fd.getFile());

                    this.redesign((oldContent) -> {
                        JFContainer oldContainer = (JFContainer) oldContent;
                        textComponent = new JFText(selectedFile.getName());
                        return new JFContainer(
                                oldContent.getWidth(),
                                oldContent.getHeight(),
                                oldContainer.getDecoration().setColor(Color.green)
                        ).addChild(
                                new JFCenter(textComponent)
                        );
                    });
                }
            }

        });
    }

    public File getSelectedFile() {
        return selectedFile;
    }

}
