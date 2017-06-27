package com.hongshaohua.jtools.http.common;

/**
 * Created by Aska on 2017/6/19.
 */
public class CommonRequest {

    private String token;

    public CommonRequest() {
    }

    public CommonRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
