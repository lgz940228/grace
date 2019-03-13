package com.lgz.grace.api.utils.httpclient;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class HttpsUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpsUtils.class);

    public static String postConnection(String postURL, String requestBody,
                                  String sendCharset, String readCharset,int connectTimeout,int readTimeout,SortedMap reqHead)throws Exception {
        // Post请求的url，与get不同的是不需要带参数
        HttpURLConnection httpConn = null;
        try {

            if (!postURL.contains("https:")) {
                URL postUrl = new URL(postURL);
                // 打开连接
                httpConn = (HttpURLConnection) postUrl.openConnection();
            } else {
                SslConnection urlConnect = new SslConnection();
                httpConn = (HttpURLConnection) urlConnect.openConnection(postURL);
            }

//			 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
//			 http正文内，因此需要设为true, 默认情况下是false;
            httpConn.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpConn.setDoInput(true);
            // 设定请求的方法为"POST"，默认是GET
            httpConn.setRequestMethod("POST");
            // Post 请求不能使用缓存
            httpConn.setUseCaches(false);
            //进行跳转
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded; charset=" + sendCharset);

            if(reqHead!=null&&reqHead.size()>0){
                Iterator iterator =reqHead.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    String val = (String)reqHead.get(key);
                    httpConn.setRequestProperty(key,val);
                }
            }

            // 设定传送的内容类型是可序列化的java对象
            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException) httpUrlConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");
//			连接主机的超时时间（单位：毫秒）
            httpConn.setConnectTimeout(1000 * connectTimeout);
//			从主机读取数据的超时时间（单位：毫秒）
            httpConn.setReadTimeout(1000 * readTimeout);
            // 连接，从postUrl.openConnection()至此的配置必须要在 connect之前完成，
            // 要注意的是connection.getOutputStream会隐含的进行 connect。
            httpConn.connect();
            DataOutputStream out = new DataOutputStream(httpConn.getOutputStream());
            out.write(requestBody.getBytes(sendCharset));
            out.flush();
            out.close();
            int status = httpConn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                logger.error("发送请求失败，状态码：[" + status + "] 返回信息：" + httpConn.getResponseMessage());
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn
                    .getInputStream(), readCharset));
            StringBuffer responseSb = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                responseSb.append(line.trim());
            }
            reader.close();
            return responseSb.toString().trim();
        } finally {
            httpConn.disconnect();
        }
    }

    public static String postConnection(String postURL,String requestBody){
        try{
            return postConnection(postURL,requestBody,"UTF-8","UTF-8",10000,60000,null);
        } catch (Exception e){
            logger.error("发送POST请求失败, 请求地址["+postURL+"], 请求参数["+requestBody+"]", e);
        }
        return null;
    }

    /**
	 * 参数签名算法
	 * 2016年8月3日 下午5:37:11
	 * @author 杨英杰
	 * @param key
	 * @param paramMap
	 * @return
	 */
	public static String getSign(String key,Map<String,String> paramMap){

        TreeMap<String,String> signMap = new TreeMap<String,String>();
        for (String paramKey : paramMap.keySet()) {
            signMap.put(paramKey, paramMap.get(paramKey));
        }
        String content = "";
        for (String keyName : signMap.keySet()) {
            content += keyName + "=" + signMap.get(keyName) + ";";
        }
        return DigestUtils.md5Hex(content + "key=" + key);

    }

    public static String getSignWithSalt(String salt, Map<String,String> paramMap){

        TreeMap<String,String> signMap = new TreeMap<String,String>();
        for (String paramKey : paramMap.keySet()) {
            signMap.put(paramKey, paramMap.get(paramKey));
        }
        String content = "";
        for (String keyName : signMap.keySet()) {
            content += keyName + "=" + signMap.get(keyName) + ";";
        }
        return DigestUtils.md5Hex(content + "key=" + salt);

    }

    public static String wrapRequestBody(Map<String,String> paramMap) throws UnsupportedEncodingException {
        String requestBody = "";
        for (String key1 : paramMap.keySet()) {
            String name = key1;
            String value = paramMap.get(key1);
            if(StringUtils.isNotEmpty(value)){
                value = URLEncoder.encode(value, "utf-8");
            }
            requestBody += name + "=" + value + "&";
        }
        if (requestBody.endsWith("&")) {
            requestBody = StringUtils.removeEnd(requestBody, "&");
        }
        return requestBody;
    }

}
