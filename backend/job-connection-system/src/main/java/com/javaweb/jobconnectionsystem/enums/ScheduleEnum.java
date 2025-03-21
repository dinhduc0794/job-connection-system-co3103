package com.javaweb.jobconnectionsystem.enums;

public enum ScheduleEnum {
    FULLTIME("Full-time"),
    PARTTIME("Part-time"),
    INTERNSHIP("Internship"),
    FREELANCE("Freelance"),
    CONTRACT("Contract");

    private String value;

    ScheduleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
