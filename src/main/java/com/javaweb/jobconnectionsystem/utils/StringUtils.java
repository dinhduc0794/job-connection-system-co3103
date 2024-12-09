package com.javaweb.jobconnectionsystem.utils;

public class StringUtils {
    public static Boolean notEmptyData(String data) {
        if (data != null && !data.equals("")) return true;
        else return false;
    }
}
