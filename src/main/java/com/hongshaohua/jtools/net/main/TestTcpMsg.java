package com.hongshaohua.jtools.net.main;

import com.hongshaohua.jtools.tcp.server.defaults.DefaultTcpMsg;

/**
 * Created by Aska on 2018/2/5.
 */
public class TestTcpMsg extends DefaultTcpMsg {

    private int i;

    public TestTcpMsg() {
    }

    public TestTcpMsg(int id, int i) {
        super(id);
        this.i = i;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
