package com.lgz.grace.api.utils;

import com.alibaba.fastjson.JSON;

/**
 * Created by lgz on 2019/3/11.
 */
public class JsonUtil {

    public static Object parseJsonStr(String jsonStr){
        if(jsonStr != null){
            if(jsonStr.startsWith("{") || jsonStr.startsWith("[")){
                return JSON.parse(jsonStr);
            }else{
                try {
                    return JSON.parse(jsonStr);
                }catch (Exception e){
                    return jsonStr;
                }
            }
        }
        return null;
    }

    /*public static void main(String[] args) {
        Object o = parseJsonStr("a");
        System.out.println(o);
        Object o1 = parseJsonStr("{\"a\":3}");
        System.out.println(o1);
        Object o2 = parseJsonStr("{\"a\":\"3\"}");
        System.out.println(o2);
        Object o3 = parseJsonStr("3");
        System.out.println(o3);
        Object o4 = parseJsonStr("\"3\"");
        System.out.println(o4);
    }*/
}
