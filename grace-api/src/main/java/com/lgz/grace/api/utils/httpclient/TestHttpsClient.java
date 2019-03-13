package com.lgz.grace.api.utils.httpclient;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lgz.grace.api.utils.io.StreamUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by lgz on 2019/3/13.
 */
public class TestHttpsClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static volatile RequestConfig requestConfig;
    //private static CloseableHttpClient httpClient = HttpClients.createDefault();
    private static PoolingHttpClientConnectionManager connectionPool;
    private static ServiceUnavailableRetryStrategy serviceUnavailableRetryStrategy = new NoSuccessStatusRetryStrategy.Builder().executionCount(3).retryInterval(1000).build();
    private static CloseableHttpClient httpClient = HttpClients.custom().setServiceUnavailableRetryStrategy(serviceUnavailableRetryStrategy).setConnectionManager(connectionPool).build();

    static {
        // 设置连接池
        connectionPool = new PoolingHttpClientConnectionManager(/*getRegistry()*/);
        // 设置连接池大小
        connectionPool.setMaxTotal(50);
        connectionPool.setDefaultMaxPerRoute(connectionPool.getMaxTotal());
        // 在提交请求之前 测试连接是否可用
        connectionPool.setValidateAfterInactivity(1000);
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

    public static String doGet(String url){
        try {
            HttpGet httpget = new HttpGet(url);
            httpget.setConfig(getRequestConfig());
            return httpClient.execute(httpget,rh());
        }catch (Exception e){
            logger.error("httpclient doGet error:",e);
        }finally {
            StreamUtil.close(httpClient);
        }
        return null;
    }

    public static String doPost(String url,Map<String,String> param,Map<String,String> headers){
        try {
            HttpPost httpPost = new HttpPost(url);
            List<BasicNameValuePair> list = Lists.newArrayList();
            if(param != null && param.size()>0){
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (StringUtils.isEmpty(value)) {
                        continue;
                    }
                    list.add(new BasicNameValuePair(key, value));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"UTF-8");
                httpPost.setEntity(entity);
            }
            if(headers != null && headers.size()>0){
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (StringUtils.isEmpty(value)) {
                        continue;
                    }
                    httpPost.setHeader(key,value);
                }
            }
            httpPost.setConfig(getRequestConfig());
            return httpClient.execute(httpPost,rh());
        }catch (Exception e){
            logger.error("httpclient doPost error:",e);
        }finally {
            StreamUtil.close(httpClient);
        }
        return null;
    }

    public static String doMultipart(String url,Map<String, String> param,Map<String,String> headers,Map<String, MultipartFile> fileMap){
        try {
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            if(fileMap != null && fileMap.size()>0) {
                for (Map.Entry<String, MultipartFile> entryFile : fileMap.entrySet()) {
                    InputStream inputStream = entryFile.getValue().getInputStream();
                    String fileName = entryFile.getValue().getOriginalFilename();
                    InputStreamBody inputStreamBody = new InputStreamBody(inputStream, fileName);
                    builder.addPart(fileName, inputStreamBody);
                }
            }
            if(param != null && param.size()>0) {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (StringUtils.isEmpty(value)) {
                        continue;
                    }
                    builder.addTextBody(key, value);
                }
            }
            if(headers != null && headers.size()>0){
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (StringUtils.isEmpty(value)) {
                        continue;
                    }
                    httpPost.setHeader(key,value);
                }
            }
            httpPost.setEntity(builder.build());
            httpPost.setConfig(getRequestConfig());
            return httpClient.execute(httpPost,rh());
        }catch (Exception e){
            logger.error("httpclient doPost error:",e);
        }finally {
            StreamUtil.close(httpClient);
        }
        return null;
    }



    public static ResponseHandler<String> rh(){
        ResponseHandler<String> rh = new ResponseHandler<String>() {
            @Override
            public String handleResponse(
                    final HttpResponse response) throws IOException {
                HttpEntity entity = response.getEntity();
                StatusLine statusLine = response.getStatusLine();
                try {
                    String s = null;
                    if(statusLine.getStatusCode() == 200){
                        s = EntityUtils.toString(entity,"UTF-8");
                        logger.error("HttpClient response content={}",s);
                    }else {
                        logger.error("httpclient 请求失败:statusLine={}", JSON.toJSONString(statusLine));
                        throw new RuntimeException("调用失败 statusLine="+JSON.toJSONString(statusLine));
                    }
                /*if (entity != null) {
                    long len = entity.getContentLength();
                    if (len != -1 && len < 2048) {
                        s = EntityUtils.toString(entity,"UTF-8");
                        System.out.println(s);
                    } else {
                        // Stream content out
                    }
                }*/
                    return s;
                }catch (Exception e) {
                    logger.error("HttpClient error statusLine={}:",response.getStatusLine(),e);
                }finally {
                    EntityUtils.consume(entity);
                    StreamUtil.close((CloseableHttpResponse)response);
                }
                return null;
            }
        };
        return rh;
    }

    public final static void main(String[] args) throws Exception {
        /*String url = "http://httpbin.org/";
        doGet(url);*/

        String urls = "https://wwwtest3.maimaiche.com/common/getAllCity.do";
        String s = doPost(urls, null, null);
        System.out.println(s);
        //doMultipart(urls,null,null,null);
    }

    /*private static class MySSLSocketFactory extends SSLConnectionSocketFactory {
        public MySSLSocketFactory(SSLContext sslContext)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(sslContext);
            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslContext.init(null, new TrustManager[] { tm }, null);
        }
    }*/

    /*private static Registry<ConnectionSocketFactory> getRegistry(){
        try {
            // Trust own CA and all self-signed certs
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(trustStore,new TrustSelfSignedStrategy())
                    .build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new MySSLSocketFactory(
                    sslcontext);
            return RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslsf)
                    .build();
        }catch (Exception e){
            return null;
        }

    }*/
}
