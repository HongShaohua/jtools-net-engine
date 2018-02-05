package com.hongshaohua.jtools.tcp.server.defaults;

import com.hongshaohua.jtools.tcp.server.TcpServerDecoder;
import com.hongshaohua.jtools.tcp.server.TcpServerEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aska on 2018/2/2.
 */
public class DefaultTcpHandlerMap extends DefaultTcpHandler<DefaultTcpMsg> {

    private final static Logger logger = LoggerFactory.getLogger(DefaultTcpHandlerMap.class);

    private Map<Integer, DefaultTcpHandlerMsg> recvHandlers;
    private Map<Integer, DefaultTcpHandlerMsg> sendHandlers;

    public DefaultTcpHandlerMap(List<DefaultTcpHandlerMsg> recvHandlers, List<DefaultTcpHandlerMsg> sendHandlers) {
        this.recvHandlers = new HashMap<>();
        for(DefaultTcpHandlerMsg recvHandler : recvHandlers) {
            this.recvHandlers.put(recvHandler.getId(), recvHandler);
        }
        this.sendHandlers = new HashMap<>();
        for(DefaultTcpHandlerMsg sendHandler : sendHandlers) {
            this.sendHandlers.put(sendHandler.getId(), sendHandler);
        }
    }

    @Override
    public TcpServerDecoder newDecoder() {
        return new TcpServerDecoder(this);
    }

    @Override
    public TcpServerEncoder<DefaultTcpMsg> newEncoder() {
        return new DefaultTcpMsgEncoder<>(this);
    }

    @Override
    protected DefaultTcpMsg deserialize(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        //buf第一个int为len，第二个int为id
        int id = buf.getInt(INT_SIZE * 2);
        DefaultTcpHandlerMsg handler = this.recvHandlers.get(id);
        if(handler == null) {
            logger.error("deserialize handler error, id: " + id + " not exists");
            return null;
        }
        return (DefaultTcpMsg)handler.deserialize(ctx, buf);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void received(ChannelHandlerContext ctx, DefaultTcpMsg msg) throws Exception {
        int id = msg.getId();
        DefaultTcpHandlerMsg handler = this.recvHandlers.get(id);
        if(handler == null) {
            logger.error("received handler error, id: " + id + " not exists");
            return;
        }
        handler.received(ctx, msg);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ByteBuf serialize(ChannelHandlerContext ctx, DefaultTcpMsg msg) throws Exception {
        int id = msg.getId();
        DefaultTcpHandlerMsg handler = this.sendHandlers.get(id);
        if(handler == null) {
            logger.error("serialize handler error, id: " + id + " not exists");
            return null;
        }
        return handler.serialize(ctx, msg);
    }
}
