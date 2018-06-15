package com.hongshaohua.jtools.tcp.defaults;

import com.hongshaohua.jtools.tcp.common.TcpEncoder;

/**
 * Created by Aska on 2018/2/5.
 */
public class DefaultTcpHandlerMapEncoder extends TcpEncoder<DefaultTcpMsg> {

    public DefaultTcpHandlerMapEncoder(DefaultTcpHandlerMap handlerMap) {
        super(handlerMap);
    }
}
