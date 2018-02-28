package com.hongshaohua.jtools.tcp.defaults;

import com.hongshaohua.jtools.tcp.common.TcpDecoder;
import com.hongshaohua.jtools.tcp.common.TcpEncoder;
import com.hongshaohua.jtools.tcp.common.TcpHandler;
import com.hongshaohua.jtools.tcp.common.TcpInitializer;

/**
 * Created by Aska on 2018/2/6.
 */
public class DefaultTcpHandlerMapInitializer implements TcpInitializer {

    private DefaultTcpHandlerMap handlerMap;

    public DefaultTcpHandlerMapInitializer(DefaultTcpHandlerMap handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public TcpDecoder decoder() {
        return new TcpDecoder(this.handlerMap);
    }

    @Override
    public TcpEncoder<DefaultTcpMsg> encoder() {
        return new DefaultTcpHandlerMapEncoder(this.handlerMap);
    }

    @Override
    public TcpHandler<DefaultTcpMsg> handler() {
        return this.handlerMap;
    }
}
