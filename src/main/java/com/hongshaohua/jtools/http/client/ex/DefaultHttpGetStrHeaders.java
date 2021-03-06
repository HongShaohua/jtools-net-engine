package com.hongshaohua.jtools.http.client.ex;

import com.hongshaohua.jtools.http.client.DefaultHttpGetString;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * Created by Aska on 2017/9/25.
 */
public class DefaultHttpGetStrHeaders extends DefaultHttpGetString {

    private Header[] headers;

    public DefaultHttpGetStrHeaders(String url, String charset) {
        super(url, charset);
    }

    public DefaultHttpGetStrHeaders(String url) {
        super(url);
    }

    public Header[] headers() {
        return headers;
    }

    protected void headers(Header[] headers) {
        this.headers = headers;
    }

    @Override
    protected HttpContext createContext() throws Exception {
        return new BasicHttpContext();
    }

    @Override
    protected boolean response(HttpResponse response) throws Exception {
        if(!super.response(response)) {
            return false;
        }

        this.headers(response.getAllHeaders());

        return true;
    }
}
