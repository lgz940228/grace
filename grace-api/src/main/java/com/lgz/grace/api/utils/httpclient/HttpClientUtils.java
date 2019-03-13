package com.lgz.grace.api.utils.httpclient;

import com.lgz.grace.api.utils.other.StringUtils;
import com.lgz.grace.api.utils.other.SysUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    final static private Integer connectionTimeout = 10000;

    final static private Integer soTimeout = 180000;

    private final static MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();

    static {
        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        params.setMaxTotalConnections(200);
        params.setDefaultMaxConnectionsPerHost(15);
        multiThreadedHttpConnectionManager.setParams(params);
    }

    public static void getResponseBodyAsyn(final String uriPreFix, final String uri,
                                           final Map<String, Object> params, final String sessionId) {
        executor.execute(new Runnable() {
            public void run() {
                try {
                    getResponseBody(uriPreFix, uri, params, sessionId);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
    }

    public static String getResponseBody(String uriPreFix, String uri, Map<String, Object> params,
                                         Integer connectionTimeoutParam, Integer soTimeoutParam, String sessionId, MethodEnum methodEnum) {
        String responseBody = "";
        String url = StringUtils.checkEmpty(uriPreFix) != null ? uriPreFix.concat(uri) : uri;
        HttpClient client = new HttpClient(multiThreadedHttpConnectionManager);
        HttpMethod method;
        if (methodEnum == null || methodEnum == MethodEnum.POST)
            method = new PostMethod(url);
        else
            method = new GetMethod(url);

        if (params != null && params.size() > 0) {
            NameValuePair[] data = new NameValuePair[params.size()];
            int i = 0;
            String key;
            Object value;
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                data[i] = new NameValuePair(key, String.valueOf(value));
                i++;
            }
            method.setQueryString(data);
        }
        //设置sessionId
        if (StringUtils.checkEmpty(sessionId) != null) {
            String defaultSessionCookieName = "carweb";
            String sessionCookieName = SysUtil.getResourceValue("sessionCookieName");
            if (StringUtils.checkEmpty(sessionCookieName) == null) {
                sessionCookieName = defaultSessionCookieName;
            }
            method.setRequestHeader("Cookie", sessionCookieName + "=\"" + sessionId + "\"");
        }
        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        client.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeoutParam);
        client.getHttpConnectionManager().getParams().setSoTimeout(soTimeoutParam);
        try {
            client.executeMethod(method);
            responseBody = method.getResponseBodyAsString();
            //logger.error("验证结果-------" + responseBody);
            //System.out.println(responseBody);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            method.releaseConnection();
        }
        return responseBody;

    }

    public static String getResponseBody(String uriPreFix, String uri, Map<String, Object> params,
                                         Integer connectionTimeoutParam, Integer soTimeoutParam, String sessionId) {
        return getResponseBody(uriPreFix, uri, params,
                connectionTimeoutParam, soTimeoutParam, sessionId, null);

    }

    public static String getResponseBody(String url, String param, int timeOut, String sessionId) {
        if (StringUtils.checkEmpty(param) != null) {
            url = url.concat(param);
        }
        return getResponseBody(url, timeOut, sessionId);
    }

    public static String getResponseBody(String uri, int timeOut, String sessionId) {
        return getResponseBody(null, uri, null, timeOut, timeOut, sessionId);
    }

    public static String getResponseBody(String uri, Map<String, Object> params, String sessionId) {
        return getResponseBody(null, uri, params, sessionId);
    }

    public static String getResponseBody(String uriPreFix, String uri, Map<String, Object> params, String sessionId) {
        return getResponseBody(uriPreFix, uri, params, connectionTimeout, soTimeout, sessionId);
    }

    public enum MethodEnum {
        GET, POST;
    }
}
