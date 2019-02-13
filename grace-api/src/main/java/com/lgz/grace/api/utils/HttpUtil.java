package com.lgz.grace.api.utils;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class HttpUtil {
    public static String post(String url, String content, int connectTimeout, int readTimeout) {
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }
        String str = null;
        try {
            str = DefaultHttpClient.doPost(url, "application/octet-stream", content.getBytes(),
                    connectTimeout, readTimeout);
            return str;
        } catch (FrameworkException e) {
            //ExceptionUtil.throwRunTime(e);
        }
        return null;
    }

    public static String post(String url, Map<String, String> params, int connectTimeout, int readTimeout) {
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }
        String str = null;
        try {
            str = DefaultHttpClient.doPost(url, params, connectTimeout, readTimeout);
            return str;
        } catch (FrameworkException e) {
           // ExceptionUtil.throwRunTime(e);
        }
        return null;
    }

    public static String post(String url, Map<String, String> params, List<Cookie> cookies,
                              int connectTimeout, int readTimeout) throws Exception {
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }
        PostMethod postMethod = new PostMethod(url);
        if (params != null) {
            for (String key : params.keySet()) {
                if (key != null) {
                    postMethod.setParameter(key, params.get(key));
                }
            }
        }
        if (cookies != null) {
            StringBuilder cookieValue = new StringBuilder(512);
            for (Cookie cookie : cookies) {
                cookieValue.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
            }
            if (cookieValue.length() > 0) {
                postMethod.addRequestHeader(new Header("Cookie", cookieValue.toString()));
            }
        }
        HttpClient httpClient = new HttpClient();
        HttpConnectionManagerParams managerParams = httpClient.getHttpConnectionManager().getParams();
        // 设置连接超时时间(单位毫秒)
        managerParams.setConnectionTimeout(connectTimeout);

        // 设置读数据超时时间(单位毫秒)
        managerParams.setSoTimeout(readTimeout);
        int status = httpClient.executeMethod(postMethod);
        if (status == 200) {
            String responseBody = postMethod.getResponseBodyAsString();
            return responseBody;
        } else {
            return "code=" + status;
        }
    }

    /**
     * 根据URL 和编码格式 获取指定url返回的信息
     *
     * @param urlStr
     * @param charset
     * @return
     * @throws Exception
     */
    public static String getContent(String urlStr, String charset) throws RuntimeException {
        return getContent(urlStr, charset, null, null);
    }

    /**
     * 根据URL 和编码格式 获取指定url返回的信息
     *
     * @param urlStr
     * @param charset
     * @return
     * @throws Exception
     */
    public static String getContent(String urlStr, String charset, Integer connectTimeout, Integer readTimeout) throws RuntimeException {
        if (!urlStr.startsWith("http://")) {
            urlStr = "http://" + urlStr;
        }
        BufferedReader bufferedReader = null;
        StringBuilder builder = new StringBuilder(50 * 1024);
        try {
            URL url = new URL(urlStr);
            InputStream is;
            if (connectTimeout == null || readTimeout == null) {
                is = url.openStream();
            } else {
                URLConnection connect = url.openConnection();
                connect.setConnectTimeout(connectTimeout);
                connect.setReadTimeout(readTimeout);
                is = connect.getInputStream();
            }
            bufferedReader = new BufferedReader(new InputStreamReader(is, charset));
            builder = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return builder.toString();
    }
}
