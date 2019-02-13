package com.lgz.grace.api;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHttpResponse;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by lgz on 2019/1/10.
 */
public class HttpClientTest {
    @org.junit.Test
    public void getUri() throws URISyntaxException{
        URI uri = new URIBuilder()
        .setScheme("http")
        .setHost("www.google.com")
        .setPath("/search")
        .setParameter("q","httpclient")
        .setParameter("btnG","Google搜索")
        .setParameter("aq","f")
        .setParameter("oq","")
        .build();
        HttpGet httpget = new HttpGet(uri);
        System.out.println(httpget.getURI());
    }

    @org.junit.Test
    public void httpResponse(){
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        System.out.println(response.getStatusLine().getProtocolVersion());
        System.out.println(response.getStatusLine().getReasonPhrase());
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(response.getStatusLine().toString());
    }
    @org.junit.Test
    public void clientCustom(){
        CloseableHttpClient build = HttpClients.custom().build();
    }

}
