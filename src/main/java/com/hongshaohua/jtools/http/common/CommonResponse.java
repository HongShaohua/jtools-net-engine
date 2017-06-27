package com.hongshaohua.jtools.http.common;

/**
 * Created by Aska on 2017/6/19.
 */
public class CommonResponse {

    private int code;
    private String msg;

    public CommonResponse() {
    }

    public CommonResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
