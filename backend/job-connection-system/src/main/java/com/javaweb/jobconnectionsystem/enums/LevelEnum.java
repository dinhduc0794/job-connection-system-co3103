package com.javaweb.jobconnectionsystem.enums;

public enum LevelEnum {
    INTERN("Intern"),          // Thực tập sinh
    FRESHER("Fresher"),        // Nhân viên mới, ít kinh nghiệm
    JUNIOR("Junior"),          // Nhân viên có kinh nghiệm cơ bản
    SENIOR("Senior"),          // Nhân viên dày dạn kinh nghiệm
    LEAD("Lead"),              // Trưởng nhóm
    MANAGER("Manager"),        // Quản lý
    DIRECTOR("Director");       // Giám đốc

    private String value;

    LevelEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}