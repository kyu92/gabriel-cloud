package com.kyu.gabriel.core.string;

import java.text.MessageFormat;
import java.util.Locale;

public abstract class StringUtils {

    public static boolean isBlank(String s) {
        return s.trim().equalsIgnoreCase("");
    }

    public static boolean isEmpty(String s) {
        if (s == null) {
            return true;
        }
        return isBlank(s);
    }

    public static String getFileNameWithoutSuffix(String fileName) {
        if (fileName == null) {
            return "";
        }
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    public static String getFileSuffix(String fileName) {
        if (fileName == null) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.'));
    }
}
