package com.lgz.grace.api.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by GrayF on 2016/5/19.
 */
public class Json2MapUtil {

    /**
     * 将JSON字符串转换为JAVA-MAP
     * @param jsonStr
     * @return
     */
    public static Map<String,Object> transform(String jsonStr){
        Map<String,Object> resultMap = null;
        if(StringUtils.isNotEmpty(jsonStr)){
            Map<String,Object> josnObjectMap = JSON.parseObject(jsonStr,Map.class);
            if(josnObjectMap != null && josnObjectMap.size() > 0){
                for(String key : josnObjectMap.keySet()){
                    Object obj = josnObjectMap.get(key);
                    if(obj instanceof JSONObject){
                        obj = JSONObject2Map((JSONObject)obj);
                    }else if(obj instanceof JSONArray){
                        obj = JSONArray2ListMap((JSONArray)obj);
                    }
                    //如果结果为空，则不生成该键值数据
                    if(obj != null){
                        if(resultMap == null){
                            resultMap = Maps.newHashMap();
                        }
                        resultMap.put(key,obj);
                    }
                }
            }
        }
        return resultMap;
    }

    public static Map<String,Object> JSONObject2Map(JSONObject jsonObject){
        if(jsonObject != null){
            return transform(jsonObject.toJSONString());
        }
        return null;
    }

    public static Object JSONArray2ListMap(JSONArray jsonArray){
        if(jsonArray != null && jsonArray.size() > 0){
            if(jsonArray.get(0) instanceof JSONObject){
                List<Map<String,Object>> list = Lists.newArrayList();
                for(int i = 0; i < jsonArray.size() ; i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(JSONObject2Map(jsonObject));
                }
                return list;
            }else{
                return jsonArray.subList(0,jsonArray.size());
            }
        }
        return null;
    }
}

