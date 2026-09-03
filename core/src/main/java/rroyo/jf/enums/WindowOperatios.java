package rroyo.jf.enums;

import lombok.Getter;

public enum WindowOperatios {
    DO_NOTHING_ON_CLOSE(0),
    HIDE_ON_CLOSE(1),
    DISPOSE_ON_CLOSE(2),
    EXIT_ON_CLOSE(3)
    ;

    @Getter
    private final int value;

    private WindowOperatios(int value) {
        this.value = value;
    }

}
