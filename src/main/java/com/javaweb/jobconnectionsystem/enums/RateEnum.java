package com.javaweb.jobconnectionsystem.enums;

public enum RateEnum {
    ONE_STAR("1"),
    TWO_STAR("2"),
    THREE_STAR("3"),
    FOUR_STAR("4"),
    FIVE_STAR("5");

    private String value;

    RateEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
