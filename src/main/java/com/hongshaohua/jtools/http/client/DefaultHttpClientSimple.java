package com.hongshaohua.jtools.http.client;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aska on 2017/6/18.
 */
public class DefaultHttpClientSimple extends DefaultHttpClient {

    public DefaultHttpClientSimple() {
        this.setHeaders(defaultHeaders());
    }

    private List<Header> defaultHeaders() {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"));
        headers.add(new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"));
        headers.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3"));
        headers.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
        headers.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
        return headers;
    }
}
