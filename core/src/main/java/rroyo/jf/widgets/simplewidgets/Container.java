package rroyo.jf.widgets.simplewidgets;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import rroyo.JUtils.Utils.Logging.LoggerAux;
import rroyo.jf.decorations.Decoration;
import rroyo.jf.widgets.childcomponent.SingleChildComponent;

import java.awt.*;
import java.net.http.HttpHeaders;

public class Container extends SizedBox {

    @Getter
    private final Decoration decoration;

    public Container(int width, int height, @Nullable Color color) {
        super(width, height);
        this.decoration = new Decoration(color, this);
    }

    public Container(Dimension dimension, Decoration decoration) {
        super(dimension);
        this.decoration = new Decoration(decoration.getColor(), this)
                .setBorder(decoration.getBorder())
                .setBorderRadius(decoration.getBorderRadius())
                .setShadow(decoration.getShadow());
    }

    @Override
    protected void design(Graphics g) {
        if (decoration.getColor() == null) {
            LoggerAux.warn("Container can be replaced with 'SizedBox'");
            return;
        }

        decoration.draw(g, 0, 0, getSize().width, getSize().height);
    }
}
