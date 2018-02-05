package com.hongshaohua.jtools.tcp.server.defaults;

import com.hongshaohua.jtools.common.reflect.ReflectDataClass;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;

/**
 * Created by Aska on 2018/2/5.
 */
public abstract class DefaultTcpHandlerMsgAutoWrite<T extends DefaultTcpMsg> extends DefaultTcpHandlerMsg<T> {

    private final static Logger logger = LoggerFactory.getLogger(DefaultTcpHandlerMsgAutoWrite.class);

    private ReflectDataClass genericClass = null;

    public DefaultTcpHandlerMsgAutoWrite(int id) {
        super(id);
        this.parseGenericClass();
    }

    private Class getChildClass() {
        Class childClass = null;
        Class curClass = this.getClass();
        while(true) {
            if(curClass.equals(DefaultTcpHandlerMsgAutoWrite.class)) {
                break;
            }
            childClass = curClass;
            curClass = curClass.getSuperclass();
        }
        return childClass;
    }

    private void parseGenericClass() {
        Class childClass = this.getChildClass();
        this.genericClass = ReflectDataClass.parseClass(((ParameterizedType)(childClass.getGenericSuperclass())).getActualTypeArguments()[0]);
    }

    @Override
    protected T deserialize(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        return null;
    }

    @Override
    protected ByteBuf serialize(ChannelHandlerContext ctx, T msg) throws Exception {
        return null;
    }
}
