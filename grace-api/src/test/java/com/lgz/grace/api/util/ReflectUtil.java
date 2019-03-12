package com.lgz.grace.api.util;

import com.lgz.grace.api.annotation.Excel;
import com.lgz.grace.api.entities.Record;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgz on 2019/3/12.
 */
public class ReflectUtil {

    /**
     * 获取class的 包括父类的
     *
     * @param clazz
     * @return
     */
    public static Field[] getClassFields(Class<?> clazz) {
        List<Field> list = new ArrayList();
        Field[] fields;
        do {
            fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                list.add(fields[i]);
            }
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class && clazz != null);
        return list.toArray(fields);
    }

    @Test
    public void getClassFieldsTest(){
        Field[] classFields = getClassFields(Record.class);
        Field classField = classFields[1];
        Excel annotation = classField.getAnnotation(Excel.class);
        String s = annotation.databaseFormat();
        System.out.println(s);
        System.out.println("aaa");

    }
}
