package com.lgz.grace.api.util;

import com.lgz.grace.api.Person;
import com.lgz.grace.api.annotation.Excel;
import com.lgz.grace.api.entities.Record;
import org.junit.Test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
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
    public void getset()throws Exception{
        Person person = new Person();
        person.setName("小红");
        person.setMobile("15222222222");
        person.setSex("女");
        BeanInfo beanInfo = Introspector.getBeanInfo(person.getClass(),Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : propertyDescriptors){
            if(pd.getPropertyType().isArray())  //getPropertyType得到属性类型。
            {
                //getReadMethod()得到此属性的get方法----Method对象，然后用invoke调用这个方法
                String[] result=(String[]) pd.getReadMethod().invoke(person, null);
                System.out.println(pd.getName()+":");//getName得到属性名字
                for (int j = 0; j < result.length; j++) {
                    System.out.println(result[j]);
                }
            }
            else{
                if("id".equals(pd.getName())){
                    pd.getWriteMethod().invoke(person,"123");
                }
                System.out.println(pd.getName()+":"+pd.getReadMethod().invoke(person, null));
            }
        }
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
