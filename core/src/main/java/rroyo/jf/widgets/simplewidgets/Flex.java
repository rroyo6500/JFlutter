package rroyo.jf.widgets.simplewidgets;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import rroyo.jf.enums.CrossAxisAlignment;
import rroyo.jf.enums.FlexDirection;
import rroyo.jf.enums.MainAxisAlignment;
import rroyo.jf.widgets.basewidgets.Widget;
import rroyo.jf.widgets.childcomponent.MultiChildComponent;

import java.awt.*;

public class Flex extends Widget implements MultiChildComponent<Flex> {

    @Getter @Setter
    protected FlexDirection direction;

    protected MainAxisAlignment mainAxisAlignment = MainAxisAlignment.DEFAULT;

    protected CrossAxisAlignment crossAxisAlignment = CrossAxisAlignment.DEFAULT;

    public Flex(FlexDirection direction) {
        super(true);
        this.direction = direction;
    }

    public MainAxisAlignment mainAxisAlignment() {
        return mainAxisAlignment;
    }
    public Flex mainAxisAlignment(MainAxisAlignment mainAxisAlignment) {
        this.mainAxisAlignment = mainAxisAlignment;
        return this;
    }

    public CrossAxisAlignment crossAxisAlignment() {
        return crossAxisAlignment;
    }
    public Flex crossAxisAlignment(CrossAxisAlignment crossAxisAlignment) {
        this.crossAxisAlignment = crossAxisAlignment;
        return this;
    }

    private int getMainSize(@NotNull Widget component) {
        return switch (direction) {
            case ROW -> component.getSize().width;
            case COLUMN -> component.getSize().height;
        };
    }

    /**
     * Obtiene el tamaño del componente en el eje cruzado.
     */
    private int getCrossSize(@NotNull Widget component) {
        return switch (direction) {
            case ROW -> component.getSize().height;
            case COLUMN -> component.getSize().width;
        };
    }

    protected float[] calculateFlexChildPositions(
            int remainingSpace,
            int childCount
    ) {

        float currentPos = 0;
        float gap = 0;

        if (childCount > 0) {
            switch (mainAxisAlignment) {
                case START, DEFAULT -> {
                    currentPos = 0;
                    gap = 0;
                }
                case CENTER -> {
                    currentPos = remainingSpace / 2f;
                    gap = 0;
                }
                case END -> {
                    currentPos = remainingSpace;
                    gap = 0;
                }
                case SPACE_BETWEEN -> {
                    currentPos = 0;
                    gap = childCount > 1
                            ? (float) remainingSpace / (childCount - 1)
                            : 0;
                }
                case SPACE_AROUND -> {
                    gap = (float) remainingSpace / childCount;
                    currentPos = gap / 2f;
                }
                case SPACE_EVENLY -> {
                    gap = (float) remainingSpace / (childCount + 1);
                    currentPos = gap;
                }
            }
        }

        return new float[]{currentPos, gap};
    }

    @Override
    protected void layoutRecalculate() {

        int totalMainSize = 0;
        int maxCrossSize = 0;
        int activeChildCount = 0;

        for (Widget child : getChildList()) {
            if (!child.isActive()) continue;

            int childMainSize = getMainSize(child);
            int childCrossSize = getCrossSize(child);

            totalMainSize += childMainSize;
            maxCrossSize = Math.max(maxCrossSize, childCrossSize);
            activeChildCount++;
        }

        int finalMainSize = totalMainSize;

        if (father != null) {

            int fatherMainSize = getMainSize(father);

            if (fatherMainSize > 0 && !(father instanceof Flex)) {
                finalMainSize = fatherMainSize;
            } else if (father instanceof AlignWidget || father instanceof Flex flex && !flex.getDirection().equals(direction)) {
                Widget sizedBox = getComponentFromTree(Container.class, SizedBox.class);
                if (sizedBox != null)
                    finalMainSize = getMainSize(sizedBox);
            }
        }

        if (direction == FlexDirection.ROW)
            setSize(finalMainSize, maxCrossSize);
        else
            setSize(maxCrossSize, finalMainSize);

        int remainingSpace = finalMainSize - totalMainSize;

        float[] flexPositions = calculateFlexChildPositions(
                remainingSpace,
                activeChildCount
        );

        float currentMainPosition = flexPositions[0];
        float gap = flexPositions[1];

        for (Widget child : getChildList()) {
            if (!child.isActive()) continue;

            int childMainSize = getMainSize(child);
            int childCrossSize = getCrossSize(child);

            int crossPosition = switch (crossAxisAlignment) {
                case CENTER -> (maxCrossSize - childCrossSize) / 2;
                case END -> maxCrossSize - childCrossSize;
                default -> 0;
            };

            if (direction == FlexDirection.ROW)
                child.setPosition(
                        Math.round(currentMainPosition),
                        crossPosition
                );
            else
                child.setPosition(
                        crossPosition,
                        Math.round(currentMainPosition)
                );

            currentMainPosition += childMainSize + gap;
        }

    }

    @Override
    protected void design(Graphics g) {
    }
}
