package com.lgz.grace.api.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class CommonUtil {
    /**
     * 获取obj中keys的数值
     *
     * @param obj
     * @param keys
     * @param <T>
     * @return
     */
    public static <T> T get(Object obj, Object... keys) {
        return get(obj, 0, keys);
    }


    private static <T> T get(Object obj, int idx, Object... keys) {
        if (obj == null) {
            return null;
        } else if (keys == null || keys.length < 1) {
            return (T) obj;
        } else if (idx >= keys.length) {
            return (T) obj;
        } else {
            Object key = keys[idx];
            if (obj instanceof Map) {
                return get(((Map) obj).get(key), idx + 1, keys);
            } else if (obj instanceof Collection) {
                Collection list = (Collection) obj;
                Integer num = NumUtil.parseInt(key);
                if (num != null && num >= 0 && num < list.size()) {
                    Iterator it = list.iterator();
                    int i;
                    for (i = 0; it.hasNext() && i < num; i++) {
                        it.next();
                    }
                    if (i == num && it.hasNext()) {
                        return get(it.next(), idx + 1, keys);
                    }
                }
            } else if (obj.getClass().isArray()) {
                Integer num = NumUtil.parseInt(key);
                if (num != null) {
                    return get(getObjByArray(obj, num), idx + 1, keys);
                }
            }
            return null;
        }
    }

    public static Object getObjByArray(Object array, int idx) {
        return ArrayUtil.getObjByIdx(array, idx);
    }


    public static String getHostIP() {
        return IPUtil.LOCAL_IP;
    }

    public static boolean anyTrue(boolean... booleans) {
        return anyBoolean(true, booleans);
    }

    public static boolean anyFalse(boolean... booleans) {
        return anyBoolean(false, booleans);
    }

    public static boolean allFalse(boolean... booleans) {
        return !anyBoolean(true, booleans);
    }

    public static boolean allTrue(boolean... booleans) {
        return !anyBoolean(false, booleans);
    }

    private static boolean anyBoolean(boolean flag, boolean... booleans) {
        for (boolean b : booleans) {
            if (b == flag) {
                return true;
            }
        }
        return false;
    }

    public static boolean anyEquals(Object obj, Object... param) {
        if (ListUtil.isNotEmpty(param)) {
            for (Object o : param) {
                if (equals(obj, o)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断所有参数是否含有一个空或无值的数据
     * 当元素是String Collection Map的或者子类时 通过更高级的判断字符串是否是Empty 集合是否包含元素 map是否包含元素
     *
     * @param objects
     * @return
     */
    public static boolean anyEmpty(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                return true;
            } else if (object instanceof String && StringUtils.isEmpty((String) object)) {
                return true;
            } else if (object instanceof Collection && ListUtil.isEmpty((Collection) object)) {
                return true;
            } else if (object instanceof Map && ListUtil.isEmpty((Map) object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断所有参数是否都是空或无值的数据
     * 当元素是String Collection Map的或者子类时 通过更高级的判断字符串是否是Empty 集合是否包含元素 map是否包含元素
     *
     * @param objects
     * @return
     */
    public static boolean allEmpty(Object... objects) {
        for (Object object : objects) {
            if (object instanceof String && StringUtils.isNotEmpty((String) object)) {
                return false;
            } else if (object instanceof Collection && ListUtil.isNotEmpty((Collection) object)) {
                return false;
            } else if (object instanceof Map && ListUtil.isNotEmpty((Map) object)) {
                return false;
            } else if (object != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断所有参数是否含有一个null
     *
     * @param objects
     * @return
     */
    public static boolean anyNull(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断所有参数是否都是null
     *
     * @param objects
     * @return
     */
    public static boolean allNull(Object... objects) {
        for (Object object : objects) {
            if (object != null) {
                return false;
            }
        }
        return true;
    }

    public static boolean neq(Object o1, Object o2) {
        return !eq(o1, o2);
    }

    public static boolean notEquals(Object o1, Object o2) {
        return neq(o1, o2);
    }

    public static boolean equals(Object o1, Object o2) {
        return eq(o1, o2);
    }

    public static boolean eq(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        } else if (o1 == null || o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }

    public static <T> T ifNull(T t1, T t2) {
        return get(t1 != null, t1, t2);
    }

    public static <T> T get(boolean flag, T t1, T t2) {
        return flag ? t1 : t2;
    }

    private CommonUtil() {

    }

    public static boolean isNull(Object o) {
        return o == null;
    }
}
