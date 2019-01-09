package com.lgz.grace.api.utils;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lyp on 2016/8/24
 */
public class PropertiesUtil {
    private static final String RESOURCE = "resource.properties";
    private static final ConcurrentHashMap<String, Properties> map = new ConcurrentHashMap<String, Properties>();
   // private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    public static  String getResourceName() {
      return RESOURCE;
    }

    //获取Properties文件指定key的value
    public static String getPropertiesValue(String name, String key) {
        Properties properties = getProperties(name);
        if (properties != null) {
            return properties.getProperty(key);
        }
        return null;
    }

    //获取Properties文件
    public static Properties getProperties(String name) {
        if (!map.containsKey(name)) {
            load(name);
        }
        return map.get(name);
    }
    //加载Properties文件
    public static void load(String name) {
        Properties properties = doLoad(name);
        map.put(name, properties);
    }

    private static Properties doLoad(String name) {
        Properties properties = new Properties();
        try {
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("/"+name));
        } catch (IOException e) {
            //logger.error("加载Properties"+name+"失败");
        }
        return properties;
    }

    public static void clear() {
        map.clear();
    }

    private PropertiesUtil() {

    }

    public static void main(String[] args) {
        System.out.println(getPropertiesValue(PropertiesUtil.getResourceName(),"ucVMURL"));
    }
}
