package com.lgz.grace.api.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.*;

/**
 * Created by guoqiang on 2016/2/14.
 * 反射工具类
 */
public class ReflectUtil {
    private static final Logger logger = LoggerFactory.getLogger(ReflectUtil.class);
    private static volatile Unsafe unsafe;

    static {
        try {
            unsafe = (Unsafe) ReflectUtil.getStaticFieldValue(Unsafe.class, "theUnsafe");
        } catch (Exception e) {
            logger.error("获取unsafe错误", e);
        }
    }

    public static List<Field> getIFieldList(Class clazz) {
        List<Field> list = new LinkedList<Field>();
        for (Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                list.add(field);
            }
        }
        return list;
    }

    public static Class getGeneric(Class clazz, Class genericClazz, int idx) {
        if (clazz == null || genericClazz == null || idx < 0 || !isSubClass(clazz, genericClazz)) {
            return null;
        }
        Class classResult = null;
        Type[] types = clazz.getGenericInterfaces();
        if (types != null) {
            for (Type type : types) {

            }
        }
        return classResult;
    }

    /**
     * 是否是子类
     *
     * @param subClass
     * @param superClass
     * @return
     */
    public static boolean isSubClass(Class subClass, Class superClass) {
        if (subClass == null || superClass == null) {
            return false;
        }
        //List.class.isAssignableFrom(ArrayList.class) =>TRUE
        return superClass.isAssignableFrom(subClass);
    }

    public static boolean isDecimal(Object obj) {
        if (obj instanceof Number) {
            if (obj instanceof Double || obj instanceof Float || obj instanceof BigDecimal) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDecimalClass(Class clazz) {
        if (clazz == double.class || clazz == float.class ||
                (clazz != null && (Double.class.isAssignableFrom(clazz)
                        || Float.class.isAssignableFrom(clazz)
                        || BigDecimal.class.isAssignableFrom(clazz)
                ))) {
            return true;
        }
        return false;
    }

    public static boolean isIntNum(Object obj) {
        if (obj instanceof Number) {
            if (obj instanceof Integer || obj instanceof Long || obj instanceof Short || obj instanceof Byte ||
                    obj instanceof BigInteger) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIntNumClass(Class clazz) {
        if (clazz == int.class || clazz == long.class || clazz == short.class || clazz == byte.class ||
                (clazz != null && (Integer.class.isAssignableFrom(clazz)
                        || Long.class.isAssignableFrom(clazz)
                        || Short.class.isAssignableFrom(clazz)
                        || Byte.class.isAssignableFrom(clazz)
                        || BigInteger.class.isAssignableFrom(clazz)
                ))) {
            return true;
        }
        return false;
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    public static Class getClass(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.getClass();
    }


    public static Class getClassByName(String className) {
        if (className == null) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(className);
            return clazz;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 获取所有实例属性的名称
     *
     * @param clazz
     * @return
     */
    public static List<String> getAllFields(Class clazz) {
        List<String> list = new ArrayList<String>();
        for (Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                list.add(field.getName());
            }
        }
        return list;
    }

    /**
     * 获取所有实例属性的名称
     *
     * @param clazz
     * @return
     */
    public static Map<String, Field> getAllFieldMap(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Field> map = new HashMap<String, Field>(fields.length);
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                map.put(field.getName(), field);
            }
        }
        return map;
    }

    /**
     * 获取obj对象的fieldName的属性的值
     *
     * @param obj
     * @param fieldName
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getFieldValue(Object obj, String fieldName) throws RuntimeException {
        Field field = null;
        try {
            field = obj.getClass().getDeclaredField(fieldName);
            return getFieldValue(obj, field);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getFieldValue(Object obj, Field field) {
        try {
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(true);
            }
            if (Modifier.isStatic(field.getModifiers())) {
                return field.get(null);
            }
            return field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class getFieldClass(Object obj, String fieldName) {
        Field field = null;
        try {
            field = obj.getClass().getDeclaredField(fieldName);
            return field.getType();
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    public static Type getFieldGenericType(Object obj, String fieldName) {
        Field field = null;
        try {
            field = obj.getClass().getDeclaredField(fieldName);
            return field.getGenericType();
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * 设置obj对象的fieldName的属性的值
     *
     * @param obj
     * @param fieldName
     * @param value
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setFieldValue(Object obj, String fieldName, Object value) throws RuntimeException {
        Field field = null;
        try {
            field = obj.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        setFieldValue(obj, field, value);
    }

    public static List<Method> getPIMethods(Class clazz, String methodName) {
        if (clazz == null || StringUtils.isEmpty(methodName)) {
            return ListUtil.emptyList();
        }
        List<Method> list = new LinkedList<Method>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && isPublic(method) && !isStatic(method)) {
                list.add(method);
            }
        }
        return list;
    }

    /**
     * 设置obj对象的fieldName的属性的值
     *
     * @param obj
     * @param field
     * @param value
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setFieldValue(Object obj, Field field, Object value) throws RuntimeException {
        try {
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(true);
            }
            if (Modifier.isFinal(field.getModifiers()) && unsafe != null) {
                long offset = unsafe.objectFieldOffset(field);
                unsafeSet(field, obj, offset, value);
            } else {
                field.set(obj, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取clazz类的fieldName的属性的值
     *
     * @param clazz
     * @param fieldName
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getStaticFieldValue(Class clazz, String fieldName) throws RuntimeException {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(true);
            }
            return field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置clazz类的fieldName的属性的值
     *
     * @param clazz
     * @param fieldName
     * @param value
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setStaticFieldValue(Class clazz, String fieldName, Object value) throws RuntimeException {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(true);
            }
            if (Modifier.isFinal(field.getModifiers()) && unsafe != null) {
                long offset = unsafe.staticFieldOffset(field);
                Object obj = unsafe.staticFieldBase(field);
                unsafeSet(field, obj, offset, value);
            } else {
                field.set(null, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getInstanceField(Class clazz, String fieldName) {
        if (clazz != null && StringUtils.isNotEmpty(fieldName)) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                if (!isStatic(field)) {
                    return field;
                }
            } catch (NoSuchFieldException e) {
            }
        }
        return null;
    }

    /**
     * 执行obj对象的methodName函数
     *
     * @param obj
     * @param methodName
     * @param param
     * @return
     * @throws NoSuchMethodException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object invokeMethod(Object obj, String methodName, Object... param) throws RuntimeException {
        Method[] methods = obj.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && !Modifier.isStatic(method.getModifiers())) {
                if (!Modifier.isPublic(method.getModifiers())) {
                    method.setAccessible(true);
                }
                try {
                    return method.invoke(obj, param);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException(obj.getClass() + " can not find method = " + methodName);
    }

    public static Method getMethod(Class clazz, String name) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        throw new RuntimeException(clazz + " can not find method = " + name);
    }

    /**
     * 执行clazz类的methodName函数
     *
     * @param clazz
     * @param methodName
     * @param param
     * @return
     * @throws NoSuchMethodException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object invokeStaticMethod(Class clazz, String methodName, Object... param) throws RuntimeException {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && Modifier.isStatic(method.getModifiers())) {
                if (!Modifier.isPublic(method.getModifiers())) {
                    method.setAccessible(true);
                }
                try {
                    return method.invoke(null, param);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException(clazz + " can not find method = " + methodName);
    }

    public static Collection<String> getInstanceMethodByPrefix(Class clazz, String methodPrefix) {
        Method[] methods = clazz.getMethods();
        Collection<String> collection = new LinkedHashSet<String>(methods.length);
        for (Method method : methods) {
            String name = method.getName();
            int m = method.getModifiers();
            if (Modifier.isPublic(m) && !Modifier.isStatic(m) && name.startsWith(methodPrefix) && name.length() > methodPrefix.length()) {
                collection.add(name);
            }
        }
        return collection;
    }

    public static Collection<String> getInstanceSetMethod(Class clazz) {
        return getInstanceMethodByPrefix(clazz, "set");
    }

    public static Collection<String> getInstanceGetMethod(Class clazz) {
        return getInstanceMethodByPrefix(clazz, "get");
    }

    private static void unsafeSet(Field field, Object obj, long offset, Object value) {
        if (field.getType() == int.class) {
            unsafe.putIntVolatile(obj, offset, (Integer) value);
        } else if (field.getType() == long.class) {
            unsafe.putLongVolatile(obj, offset, (Long) value);
        } else if (field.getType() == double.class) {
            unsafe.putDoubleVolatile(obj, offset, (Double) value);
        } else if (field.getType() == char.class) {
            unsafe.putCharVolatile(obj, offset, (Character) value);
        } else if (field.getType() == boolean.class) {
            unsafe.putBooleanVolatile(obj, offset, (Boolean) value);
        } else if (field.getType() == short.class) {
            unsafe.putShortVolatile(obj, offset, (Short) value);
        } else if (field.getType() == byte.class) {
            unsafe.putByteVolatile(obj, offset, (Byte) value);
        } else if (field.getType() == float.class) {
            unsafe.putFloatVolatile(obj, offset, (Float) value);
        } else {
            unsafe.putObjectVolatile(obj, offset, value);
        }
    }

    /**
     * 判断className是否是散落文件的类
     *
     * @param className
     * @return
     */
    public static boolean isFileClass(String className) {
        if (StringUtils.isEmpty(className)) {
            return false;
        }
        try {
            Class<?> clazz = Class.forName(className);
            return isFileClass(clazz);
        } catch (ClassNotFoundException e) {

        }
        return false;
    }

    /**
     * 判断clazz是否是散落文件的类
     *
     * @param clazz
     * @return
     */
    public static boolean isFileClass(Class clazz) {
        if (clazz != null) {
            URL url = clazz.getResource("");
            if (url != null) {
                return "file".equalsIgnoreCase(url.getProtocol());
            }
        }
        return false;
    }

    public static boolean isPublic(Member member) {
        if (member == null) {
            return false;
        }
        return Modifier.isPublic(member.getModifiers());
    }

    public static boolean isStatic(Member member) {
        if (member == null) {
            return false;
        }
        return Modifier.isStatic(member.getModifiers());
    }


    private ReflectUtil() {

    }
}
