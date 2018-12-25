package com.lgz.grace.api.utils;

import java.util.HashMap;

public class ExceptionUtil {
    public static boolean hasException(Class<? extends Exception> eClass, Throwable e) {
        if (eClass == null) {
            return false;
        }
        while (e != null) {
            if (eClass == e.getClass()) {
                return true;
            }
            e = e.getCause();
        }
        return false;
    }

    public static void main(String args[]) {
        System.out.println(HashMap.class.isAssignableFrom(HashMap.class));
    }

    public static boolean fromEx(Class<? extends Exception> eClass, Throwable e) {
        if (eClass == null) {
            return false;
        }
        while (e != null) {
            if (eClass.isAssignableFrom(e.getClass())) {
                return true;
            }
            e = e.getCause();
        }
        return false;
    }

    public static void throwRunTime(Throwable e) {
        if (e == null) {
            throw new NullPointerException("e = null");
        }
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        } else {
            throw new RuntimeException(e);
        }
    }
}
