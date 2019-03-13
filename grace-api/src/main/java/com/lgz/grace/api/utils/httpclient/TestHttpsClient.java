package com.lgz.grace.api.utils.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

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

    private static Registry<ConnectionSocketFactory> getRegistry(){
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

    }

    public final static void main(String[] args) throws Exception {
        try {
            HttpGet httpget = new HttpGet("http://httpbin.org/");
            //HttpGet httpget = new HttpGet("https://www.baidu.com");
            System.out.println("Executing request " + httpget.getRequestLine());
            httpget.setConfig(getRequestConfig());
            CloseableHttpResponse response = httpClient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                String s = EntityUtils.toString(entity);
                System.out.println(s);
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
    }

    private static class MySSLSocketFactory extends SSLConnectionSocketFactory {
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
    }
}
