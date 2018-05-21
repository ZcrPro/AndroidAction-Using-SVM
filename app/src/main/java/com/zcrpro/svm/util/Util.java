package com.zcrpro.svm.util;

public class Util {
    /**
     * 根据系统类型获取换行
     */
    public static String getChangeRow() {
        return System.getProperty("line.separator", "/n");
    }
}
