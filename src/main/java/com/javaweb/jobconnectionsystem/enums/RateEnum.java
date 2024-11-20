package com.javaweb.jobconnectionsystem.enums;

public enum RateEnum {
    ONE_STAR(1),
    TWO_STAR(2),
    THREE_STAR(3),
    FOUR_STAR(4),
    FIVE_STAR(5);

    private Integer value;

    RateEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
