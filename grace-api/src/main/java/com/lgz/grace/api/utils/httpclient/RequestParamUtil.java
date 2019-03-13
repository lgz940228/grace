package com.lgz.grace.api.utils.httpclient;

import com.google.common.collect.Lists;
import com.lgz.grace.api.utils.json.Json2MapUtil;
import com.lgz.grace.api.utils.pojo.FileMap;
import com.lgz.grace.api.utils.pojo.GatewayParam;
import com.lgz.grace.api.utils.pojo.RequestParam;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lgz on 2019/1/8.
 */
public class RequestParamUtil {

    private RequestParam getRequestParam(GatewayParam gatewayParam, HttpServletRequest request){
        InputStream is = null;
        RequestParam result = new RequestParam();
        try {
            Map<String,Object> paramMap = new HashMap<String, Object>();
            String url = null;
            //格式化请求参数
            if("GET".equals(request.getMethod())){
                url = gatewayParam.getUri();
                if(org.apache.commons.lang3.StringUtils.isNotEmpty(gatewayParam.getData())){
                    paramMap = Json2MapUtil.transform(gatewayParam.getData());
                    //查询是否设置HTML字符转义
                    boolean escapeHtmlTag = checkEscapeHtmlTag(paramMap);
                    transformParams(paramMap,escapeHtmlTag);
                }
            }else if("POST".equals(request.getMethod()) && ! (request instanceof MultipartHttpServletRequest)){
                StringBuffer sb = new StringBuffer() ;
                is = request.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
                String s = "" ;
                while((s=br.readLine())!=null){
                    sb.append(s) ;
                }
                if(sb.toString().length()>0){
                    String postParam = sb.toString();
                    Map<String,Object> postParamMap = Json2MapUtil.transform(postParam);
                    url = (String)postParamMap.get("uri");
                    Object data = postParamMap.get("data");
                    if(data != null){
                        Map<String,Object> dataMap = (Map<String,Object>)data;
                        paramMap.putAll(dataMap);
                    }
                    //查询是否设置HTML字符转义
                    boolean escapeHtmlTag = checkEscapeHtmlTag(paramMap);
                    transformParams(paramMap,escapeHtmlTag);
                }
            }
            result.setParamMap(paramMap);
            //识别是否为图片上传请求
            boolean isFile = false;
            FileMap file = new FileMap();
            if(request instanceof MultipartHttpServletRequest){
                MultipartHttpServletRequest mhs = (MultipartHttpServletRequest) request;
                url="/action/4s/uploadFiles";
                Map<String, MultipartFile> fileMap = mhs.getFileMap();
                file.putAll(fileMap);
                isFile = true;
            }
            result.setFileMap(file);
            result.setFileFlag(isFile);
            return result;
        }catch (Exception e){
            return null;
        }finally {
            if(is != null){
                try {
                    is.close();
                }catch (Exception e){
                    //logger.error("流关闭失败");
                }
            }
        }
    }




    private boolean checkEscapeHtmlTag(Map<String,Object> paramMap){
        if(paramMap != null && paramMap.size() > 0){
            Boolean escapeHtmlTag = (Boolean)paramMap.remove("escapeHtmlTag");
            if(Boolean.TRUE.equals(escapeHtmlTag)){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    private void transformParams(Map<String,Object> paramMap,boolean escapeHtmlTag){
        if(paramMap != null && paramMap.size() > 0){
            try{
                for(String key : paramMap.keySet()){
                    Object obj = paramMap.get(key);
                    if(obj != null){
                        //对象类型，递归调用
                        if (obj instanceof Map){
                            transformParams((Map<String,Object>)obj,escapeHtmlTag);
                            //数组类型，遍历
                        }else if (obj instanceof List){
                            //保存更改过的参数数组
                            List<Object> list = Lists.newArrayList();
                            for(Object obj1 : (List<Object>)obj){
                                //数组中为对象类型，递归调用
                                if(obj1 instanceof Map){
                                    transformParams((Map<String,Object>)obj1,escapeHtmlTag);
                                }else{
                                    obj1 = transformParams(obj1,escapeHtmlTag);
                                }
                                list.add(obj1);
                            }
                            obj = list;
                        }else{
                            obj = transformParams(obj,escapeHtmlTag);
                        }
                        //更新参数为转义过的数据
                        paramMap.put(key,obj);
                    }
                }
            }catch (Exception e){
                //logger.error(e.getMessage());
            }
        }
    }
    /**
     * 对参数按规则进行特殊处理
     * @param obj
     * @param escapeHtmlTag
     */
    private Object transformParams(Object obj,boolean escapeHtmlTag){
        if(obj != null){
            //将所有BigDecimal转换为Double类型
            if(obj instanceof BigDecimal){
                obj = new Double(((BigDecimal)obj).doubleValue());
            }else if (obj instanceof String && escapeHtmlTag){
                obj = StringEscapeUtils.unescapeHtml4(String.valueOf(obj));
//                obj = EscapeCharacterUtil.escapeCharacter(String.valueOf(obj));
            }
        }
        return obj;
    }
    /**
     * 判断客户端是否设置了jsonFeature 默认启用
     * @param paramMap
     * @return
     */
    private boolean checkJsonFeature(Map<String,Object> paramMap){
        if(paramMap != null){
            Boolean jsonFeature = (Boolean)paramMap.remove("jsonFeature");
            if(Boolean.FALSE.equals(jsonFeature)){
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
}
