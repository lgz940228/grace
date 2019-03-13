package com.lgz.grace.api.utils.httpclient;

import com.lgz.grace.api.utils.other.FrameworkException;
import com.lgz.grace.api.utils.other.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DefaultHttpClient {
    public static final Logger LOG = LoggerFactory.getLogger(DefaultHttpClient.class);
    public static final String DEFAULT_CHARSET = "UTF-8";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_GET = "GET";
    private static final int MAX_SIZE = 1024;

    private DefaultHttpClient() {
    }

    public static String doPost(String url, Map<String, String> params, int connectTimeout, int readTimeout) throws FrameworkException {
        return doPost(url, params, "UTF-8", connectTimeout, readTimeout);
    }

    public static String doPost(String url, Map<String, String> params, String charset, int connectTimeout, int readTimeout) throws FrameworkException {
        String ctype = "application/x-www-form-urlencoded;charset=" + charset;
        String query = buildQuery(params, charset);
        byte[] content = new byte[0];
        if (query != null) {
            try {
                content = query.getBytes(charset);
            } catch (IOException var9) {
                throw new FrameworkException(var9);
            }
        }

        return doPost(url, ctype, content, connectTimeout, readTimeout);
    }

    public static String doPost(String url, String ctype, byte[] content, int connectTimeout, int readTimeout) throws FrameworkException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;

        try {
            conn = getConnection(new URL(url), "POST", ctype);
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            out = conn.getOutputStream();
            out.write(content);
            rsp = getResponseAsString(conn);
        } catch (IOException var17) {
            Map<String, String> map = getParamsFromUrl(url);
            LOG.error((String)map.get("serviceId") + "访问出现错误.");
            throw new FrameworkException("访问远程服务失败", var17);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException var16) {
                    throw new FrameworkException(var16);
                }
            }

            if (conn != null) {
                conn.disconnect();
            }

        }

        return rsp;
    }

    public static String doGet(String url, Map<String, String> params) throws FrameworkException {
        return doGet(url, params, "UTF-8");
    }

    public static String doGet(String url, Map<String, String> params, String charset) throws FrameworkException {
        HttpURLConnection conn = null;
        String rsp = null;

        try {
            String ctype = "application/x-www-form-urlencoded;charset=" + charset;
            String query = buildQuery(params, charset);

            try {
                conn = getConnection(buildGetUrl(url, query), "GET", ctype);
            } catch (IOException var12) {
                Map<String, String> map = getParamsFromUrl(url);
                LOG.error((String)map.get("serviceId") + "访问出现错误.");
                throw new FrameworkException(var12);
            }

            rsp = getResponseAsString(conn);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }

        return rsp;
    }

    public static HttpURLConnection getConnection(URL url, String method, String ctype) {
        LOG.info("url is " + String.valueOf(url));
        LOG.info("method is " + method);
        LOG.info("ctype is" + ctype);

        try {
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html,text/plain,*/*;");
            conn.setRequestProperty("User-Agent", "remote-zuche-java");
            conn.setRequestProperty("Content-Type", ctype);
            conn.setRequestProperty("Accept-Language", "zh-CN");
            return conn;
        } catch (IOException var4) {
            LOG.error("execute open connection error", var4);
            return null;
        }
    }

    private static URL buildGetUrl(String strUrl, String query) throws IOException {
        StringBuffer buffer = new StringBuffer(strUrl);
        URL url = new URL(strUrl);
        if (StringUtils.isEmpty(query)) {
            return url;
        } else {
            if (StringUtils.isEmpty(url.getQuery())) {
                if (strUrl.endsWith("?")) {
                    buffer.append(query);
                } else {
                    buffer.append("?").append(query);
                }
            } else if (strUrl.endsWith("&")) {
                buffer.append(query);
            } else {
                buffer.append("&").append(query);
            }

            return new URL(buffer.toString());
        }
    }

    public static String buildQuery(Map<String, String> params, String charset) throws FrameworkException {
        if (params != null && !params.isEmpty()) {
            StringBuilder query = new StringBuilder();
            Set<Map.Entry<String, String>> entries = params.entrySet();
            boolean hasParam = false;
            Iterator i$ = entries.iterator();

            while(i$.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)i$.next();
                String name = (String)entry.getKey();
                String value = (String)entry.getValue();
                if (StringUtils.areNotEmpty(new String[]{name, value})) {
                    if (hasParam) {
                        query.append("&");
                    } else {
                        hasParam = true;
                    }

                    try {
                        query.append(name).append("=").append(URLEncoder.encode(value, charset));
                    } catch (IOException var10) {
                        throw new FrameworkException(var10);
                    }
                }
            }

            return query.toString();
        } else {
            return null;
        }
    }

    protected static String getResponseAsString(HttpURLConnection conn) throws FrameworkException {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        if (es == null) {
            try {
                return getStreamAsString(conn.getInputStream(), charset);
            } catch (IOException var5) {
                throw new FrameworkException(var5);
            }
        } else {
            String msg = getStreamAsString(es, charset);
            if (StringUtils.isEmpty(msg)) {
                throw new FrameworkException("返回的内容为空" + msg);
            } else {
                return msg;
            }
        }
    }

    private static String getStreamAsString(InputStream stream, String charset) throws FrameworkException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();
            char[] chars = new char[1024];
            boolean var5 = false;

            int count;
            while((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }

            String var6 = writer.toString();
            return var6;
        } catch (IOException var15) {
            throw new FrameworkException(var15);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException var14) {
                    throw new FrameworkException(var14);
                }
            }

        }
    }

    private static String getResponseCharset(String ctype) {
        String charset = "UTF-8";
        if (!StringUtils.isEmpty(ctype)) {
            String[] params = ctype.split(";");
            String[] arr$ = params;
            int len$ = params.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String param = arr$[i$];
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2 && !StringUtils.isEmpty(pair[1])) {
                        charset = pair[1].trim();
                    }
                    break;
                }
            }
        }

        return charset;
    }

    public static String decode(String value) {
        return decode(value, "UTF-8");
    }

    public static String encode(String value) {
        return encode(value, "UTF-8");
    }

    public static String decode(String value, String charset) {
        String result = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                result = URLDecoder.decode(value, charset);
            } catch (IOException var4) {
                throw new RuntimeException(var4);
            }
        }

        return result;
    }

    public static String encode(String value, String charset) {
        String result = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                result = URLEncoder.encode(value, charset);
            } catch (IOException var4) {
                throw new RuntimeException(var4);
            }
        }

        return result;
    }

    private static Map<String, String> getParamsFromUrl(String url) {
        Map<String, String> map = null;
        if (url != null && url.indexOf(63) != -1) {
            map = splitUrlQuery(url.substring(url.indexOf(63) + 1));
        }

        if (map == null) {
            map = new HashMap();
        }

        return (Map)map;
    }

    public static Map<String, String> splitUrlQuery(String query) {
        Map<String, String> result = new HashMap();
        String[] pairs = query.split("&");
        if (pairs != null && pairs.length > 0) {
            String[] arr$ = pairs;
            int len$ = pairs.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String pair = arr$[i$];
                String[] param = pair.split("=", 2);
                if (param != null && param.length == 2) {
                    result.put(param[0], param[1]);
                }
            }
        }

        return result;
    }
}
