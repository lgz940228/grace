package com.lgz.grace.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Date;

/**
 * Created by lgz on 2019/3/12.
 */
public class JsonTest {

    @org.junit.Test
    public void json(){
        String jsonStr = "{\"id\":\"1\",\"mobile\":\"15111111111\",\"name\":\"小明\"}";
        Person person = new Person();
        person.setId("1");
        person.setMobile("15111111111");
        person.setName("小明");
        person.setBirthDay(new Date());
        String s1 = JSON.toJSONString(person, SerializerFeature.WriteMapNullValue);
        String s = JSON.toJSONString(person);
        System.out.println(s1);
        System.out.println(s);
        Object parse1 = JSON.parseObject(jsonStr,Person.class);
        System.out.println(parse1);


       /* Person parse = JSON.parse(jsonStr,Person.class);
        System.out.println(parse);*/
    }
}
