package com.hongshaohua.jtools.tcp.defaults;

/**
 * Created by Aska on 2018/2/2.
 */
public abstract class DefaultTcpHandlerMsg<T extends DefaultTcpMsg> extends DefaultTcpHandler<T> {

    private int id;

    public DefaultTcpHandlerMsg(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
