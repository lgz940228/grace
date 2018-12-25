package com.lgz.grace.api.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class ArrayUtil {
    public static int getSize(Object obj) {
        if (obj instanceof Collection) {
            return ((Collection) obj).size();
        } else if (obj instanceof Map) {
            return ((Map) obj).size();
        } else if (obj != null) {
            Class<?> clazz = obj.getClass();
            if (clazz.isArray()) {
                if (obj instanceof byte[]) {
                    return ((byte[]) obj).length;
                } else if (obj instanceof boolean[]) {
                    return ((boolean[]) obj).length;
                } else if (obj instanceof char[]) {
                    return ((char[]) obj).length;
                } else if (obj instanceof short[]) {
                    return ((short[]) obj).length;
                } else if (obj instanceof int[]) {
                    return ((int[]) obj).length;
                } else if (obj instanceof long[]) {
                    return ((long[]) obj).length;
                } else if (obj instanceof float[]) {
                    return ((float[]) obj).length;
                } else if (obj instanceof double[]) {
                    return ((double[]) obj).length;
                } else if (obj instanceof Object[]) {
                    return ((Object[]) obj).length;
                }
            }
        }
        return 0;
    }


    public static Object getObjByIdx(Object array, int idx) {
        if (idx < 0) {
            return null;
        }
        if (array instanceof Collection) {
            Iterator it = ((Collection) array).iterator();
            if (it.hasNext()) {
                return it.next();
            }
        }
        if (array != null && array.getClass().isArray()) {
            if (array instanceof boolean[]) {
                if (((boolean[]) array).length <= idx) {
                    return null;
                }
                return ((boolean[]) array)[idx];
            } else if (array instanceof char[]) {
                if (((char[]) array).length <= idx) {
                    return null;
                }
                return ((char[]) array)[idx];
            } else if (array instanceof byte[]) {
                if (((byte[]) array).length <= idx) {
                    return null;
                }
                return ((byte[]) array)[idx];
            } else if (array instanceof short[]) {
                if (((short[]) array).length <= idx) {
                    return null;
                }
                return ((short[]) array)[idx];
            } else if (array instanceof int[]) {
                if (((int[]) array).length <= idx) {
                    return null;
                }
                return ((int[]) array)[idx];
            } else if (array instanceof long[]) {
                if (((long[]) array).length <= idx) {
                    return null;
                }
                return ((long[]) array)[idx];
            } else if (array instanceof float[]) {
                if (((float[]) array).length <= idx) {
                    return null;
                }
                return ((float[]) array)[idx];
            } else if (array instanceof double[]) {
                if (((double[]) array).length <= idx) {
                    return null;
                }
                return ((double[]) array)[idx];
            } else if (array instanceof Object[]) {
                if (((Object[]) array).length <= idx) {
                    return null;
                }
                return ((Object[]) array)[idx];
            } else {
                return null;
            }
        }
        return null;
    }

    private ArrayUtil() {

    }
}
