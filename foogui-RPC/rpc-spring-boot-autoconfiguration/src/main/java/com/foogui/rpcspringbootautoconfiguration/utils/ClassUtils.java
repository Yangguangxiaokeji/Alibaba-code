package com.foogui.rpcspringbootautoconfiguration.utils;

public class ClassUtils {
    public static Class<?>[] string2Class(String[] parameterTypeStrings) throws Exception {
        Class<?>[] parameterTypes = new Class<?>[parameterTypeStrings.length];
        for (int i = 0; i < parameterTypeStrings.length; i++) {
            parameterTypes[i] = Class.forName(parameterTypeStrings[i]);
        }
        return parameterTypes;
    }

    public static String[] class2String(Class<?>[] classes) {
        String[] parameterTypeString = new String[classes.length];
        for (int i = 0; i < classes.length; i++) {
            parameterTypeString[i] = classes[i].getName();
        }
        return parameterTypeString;
    }
}
