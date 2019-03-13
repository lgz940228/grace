package com.lgz.grace.api.utils.httpclient;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lgz.grace.api.utils.arithmetic.AESCryptor;
import com.lgz.grace.api.utils.arithmetic.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 *
 * @author wangyijie
 * @since 2018年07月31日
 */
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static volatile RequestConfig requestConfig;
    //private static CloseableHttpClient httpClient = HttpClients.createDefault();
    private static PoolingHttpClientConnectionManager connectionPool;
    /*private static final Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", new PlainConnectionSocketFactory())
            .register("https", SSLConnectionSocketFactory.getSocketFactory())
            .build();*/
    private static final ConnectionSocketFactory ssl = new SSLConnectionSocketFactory(SSLContexts.createDefault(),SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    private static final Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", new PlainConnectionSocketFactory())
            .register("https",ssl)
            .build();
    private static ServiceUnavailableRetryStrategy serviceUnavailableRetryStrategy = new NoSuccessStatusRetryStrategy.Builder().executionCount(3).retryInterval(1000).build();
    private static CloseableHttpClient httpClient = HttpClients.custom().setServiceUnavailableRetryStrategy(serviceUnavailableRetryStrategy).setConnectionManager(connectionPool).build();

    static {
        // 设置连接池
        connectionPool = new PoolingHttpClientConnectionManager(r);
        // 设置连接池大小
        connectionPool.setMaxTotal(50);
        connectionPool.setDefaultMaxPerRoute(connectionPool.getMaxTotal());
        // 在提交请求之前 测试连接是否可用
        connectionPool.setValidateAfterInactivity(1000);
    }

    public static void main(String[] args) throws Exception{
        HttpGet get = new HttpGet("https://www.baidu.com");
        CloseableHttpResponse execute = httpClient.execute(get);
        HttpEntity entity = execute.getEntity();
        String s = EntityUtils.toString(entity,"UTF8");
        System.out.println("-------------------------------------------------------");
        System.out.println("-------------------------------------------------------");
        System.out.println("-------------------------------------------------------");
        System.out.println("-------------------------------------------------------");
        System.out.println(s);
        execute.close();
        httpClient.close();

    }

    public static String sendRequest(String url, String secretKey, SortedMap<String, String> param) {
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> list = Lists.newArrayList();
        CloseableHttpResponse httpResponse = null;
        try {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (StringUtils.equals(key, "q")) {
                    if (StringUtils.isEmpty(value)) {
                        continue;
                    }
                    String beforeUrlEncode = AESCryptor.encrypt(value, secretKey);
                    // 特殊字符转义
                    String encode = URLEncoder.encode(beforeUrlEncode, "UTF8");
                    list.add(new BasicNameValuePair(key, encode));
                    // 加密使用的为未转义前的数据
                    entry.setValue(beforeUrlEncode);
                } else if (StringUtils.equals(key, "secretKey")) {
                    String encode = URLEncoder.encode(value, "UTF8");
                    list.add(new BasicNameValuePair(key, encode));
                } else {
                    list.add(new BasicNameValuePair(key, value));
                }
            }

            String signStr = getSignStr(param, secretKey);
            String sign = doSign(signStr);
            list.add(new BasicNameValuePair("sign", sign));

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list);
            httpPost.setEntity(entity);
            httpPost.setConfig(getRequestConfig());
            String result = null;
            httpResponse = httpClient.execute(httpPost);
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                HttpEntity responseEntity = httpResponse.getEntity();
                if (responseEntity != null) {
                    result = EntityUtils.toString(responseEntity, "UTF-8");
                }
                logger.error("Httpcliet调用ndapi返回{}",result);
                return result;
            } else {
                logger.error("httpclient调用ndapi失败:statusLine={}", JSON.toJSONString(statusLine));
                throw new RuntimeException("调用异常:url="+url+"statusLine"+statusLine);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if(httpResponse!=null){
                    EntityUtils.consume(httpResponse.getEntity());
                    httpResponse.close();
                }
            }catch (Exception e){
                logger.error("流关闭异常",e);
            }

        }

    }

    public static String sendRequestFile(String url, String secretKey, SortedMap<String, String> param,Map<String, MultipartFile> fileMap) {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse httpResponse = null;
        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            for(Map.Entry<String,MultipartFile> entryFile : fileMap.entrySet()){
                InputStream inputStream = entryFile.getValue().getInputStream();
                String fileName = entryFile.getValue().getOriginalFilename();
                InputStreamBody inputStreamBody = new InputStreamBody(inputStream, fileName);
                builder.addPart(fileName, inputStreamBody);
            }

            for (Map.Entry<String, String> entry : param.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (StringUtils.equals(key, "q")) {
                    if (StringUtils.isEmpty(value)) {
                        continue;
                    }
                    String beforeUrlEncode = AESCryptor.encrypt(value, secretKey);
                    // 特殊字符转义
                    String encode = URLEncoder.encode(beforeUrlEncode, "UTF8");
                    builder.addTextBody(key, encode);
                    // 加密使用的为未转义前的数据
                    entry.setValue(beforeUrlEncode);
                } else if (StringUtils.equals(key, "secretKey")) {
                    String encode = URLEncoder.encode(value, "UTF8");
                    builder.addTextBody(key, encode);
                } else {
                    builder.addTextBody(key, value);
                }
            }

            String signStr = getSignStr(param, secretKey);
            String sign = doSign(signStr);
            builder.addTextBody("sign", sign);

            httpPost.setEntity(builder.build());
            httpPost.setConfig(getRequestConfig());
            String result = null;
            httpResponse = httpClient.execute(httpPost);
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                HttpEntity responseEntity = httpResponse.getEntity();
                if(responseEntity != null){
                    result = EntityUtils.toString(responseEntity, "UTF-8");
                }
                logger.error("Httpcliet调用ndapi返回{}",result);
                return result;
            } else {
                logger.error("httpclient调用ndapi失败:statusLine={}", JSON.toJSONString(statusLine));
                throw new RuntimeException("调用异常");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if(httpResponse != null){
                    EntityUtils.consume(httpResponse.getEntity());
                    httpResponse.close();
                }
            }catch (Exception e){
                logger.error("流关闭异常",e);
            }
        }
    }

    private static String getSignStr(Map<String, String> map, String secriptKey) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String value = entry.getValue();
            String name = entry.getKey();
            if (StringUtils.isEmpty(value) || name.equals("key")) {
                continue;
            }
            stringBuilder.append(name).append("=").append(value).append(";");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1) + secriptKey;
    }

    private static RequestConfig getRequestConfig() {
        if (requestConfig == null) {
            synchronized (HttpClientUtil.class) {
                if (requestConfig == null) {
                    requestConfig = RequestConfig.custom().setConnectionRequestTimeout(20000)
                            .setConnectTimeout(20000).setSocketTimeout(20000).build();
                }
            }
        }
        return requestConfig;
    }

    private static String getStreamContent(InputStream inputStream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String s = reader.readLine();
        StringBuilder builder = new StringBuilder();
        while (s != null) {
            builder.append(s);
            s = reader.readLine();
        }
        return builder.toString();

    }

    private static String doSign(String string) {
        return SignUtil.doMD5Sign(string);
    }

    /**
     * 解密字符串对应的对象
     *
     * @param str       待解密对象
     * @param secretKey 密钥
     * @return
     */
    public static String decryptParam(String str, String secretKey) {
        Assert.notNull(str, "待解密对象不能为空");
        Assert.notNull(secretKey, "密钥不能为空");

        try {
            String afterEncode = URLDecoder.decode(str, "UTF8");
            return AESCryptor.decrypt(afterEncode, secretKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /*public static void main(String[] args) {
        RequestParam requestParam = new RequestParam();
        requestParam.setCid("702107");
        Map<String, Object> param = new HashMap<String,Object>();
        param.put("userName", "13003939291");
        param.put("password", "12345678");
        requestParam.setQ(param);
        String url = "http://dapitest.maimaiche.com/mmcdapi/action/4s/login";
        APIResult<String> apiResult = sendRequest(url, SECURITY_KEY,
                requestParam,null,false, APIResult.class);
        System.out.println(JSON.toJSONString(apiResult));
        System.out.println(decryptParam(apiResult.getContent(), SECURITY_KEY));
    }*/
}
