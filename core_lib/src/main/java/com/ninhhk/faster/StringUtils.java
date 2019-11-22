package com.ninhhk.faster;

public class StringUtils {
    public static String concat(String... str) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length; i++) {
            builder.append(str[i]);
        }
        return builder.toString();
    }
}
