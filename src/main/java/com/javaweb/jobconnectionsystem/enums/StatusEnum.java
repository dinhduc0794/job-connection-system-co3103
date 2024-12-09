package com.javaweb.jobconnectionsystem.enums;

public enum StatusEnum {
    WAITING("Waiting"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected");

    private String value;

    StatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
