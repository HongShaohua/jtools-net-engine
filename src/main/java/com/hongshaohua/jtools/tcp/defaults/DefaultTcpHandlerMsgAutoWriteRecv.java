package com.hongshaohua.jtools.tcp.defaults;

/**
 * Created by Aska on 2018/2/6.
 */
public abstract class DefaultTcpHandlerMsgAutoWriteRecv<T extends DefaultTcpMsg> extends DefaultTcpHandlerMsgAutoWrite<T> {

    public DefaultTcpHandlerMsgAutoWriteRecv(int id) {
        super(id);
    }
}
