package com.lgz.grace.api.utils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.net.HttpURLConnection;
import java.net.URL;

public class SslConnection {
     public HttpURLConnection openConnection(String strUrl) throws Exception {
        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        URL url = new URL(strUrl);
        return (HttpURLConnection) url.openConnection();
    }

    public String connect(String strUrl) throws Exception {

        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);

        URL url = new URL(strUrl);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        byte[] bts = new byte[100];
        urlConn.getInputStream().read(bts);
        String result = new String(bts, "UTF-8").trim();
        return result;

    }
    HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
            return true;
        }
    };

    private static void trustAllHttpsCertificates() throws Exception {

        //  Create a trust manager that does not validate certificate chains:

        javax.net.ssl.TrustManager[] trustAllCerts =
                new javax.net.ssl.TrustManager[1];

        javax.net.ssl.TrustManager tm = new miTM();

        trustAllCerts[0] = tm;

        javax.net.ssl.SSLContext sc =
                javax.net.ssl.SSLContext.getInstance("SSL");

        sc.init(null, trustAllCerts, null);

        HttpsURLConnection.setDefaultSSLSocketFactory(
                sc.getSocketFactory());

    }

    public static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) throws
                java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) throws
                java.security.cert.CertificateException {
            return;
        }
    }
}