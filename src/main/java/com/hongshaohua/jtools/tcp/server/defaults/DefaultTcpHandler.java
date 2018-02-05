package com.hongshaohua.jtools.tcp.server.defaults;

import com.hongshaohua.jtools.tcp.server.TcpServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Aska on 2018/2/5.
 */
public abstract class DefaultTcpHandler<T> implements TcpServerHandler<T> {

    private final static Logger logger = LoggerFactory.getLogger(DefaultTcpHandler.class);

    protected final static int INT_SIZE = Integer.SIZE / 8;

    public DefaultTcpHandler() {
    }

    @Override
    public void connect(ChannelHandlerContext ctx) throws Exception {

    }

    private boolean msgComplete(ByteBuf in) {
        //先根据消息长度进行分包

        //消息结构
        /**
         * len        data
         * int        bytes
         * bytes.size *********
         * */
        //len

        //先判断当前输入流中是否具备len字段
        if(in.readableBytes() < INT_SIZE) {
            //无法读取len字段
            return false;
        }
        //从流中可读位置读取一个int即为消息长度
        //为了不破坏ByteBuf中的index，使用getInt而不是readInt
        int len = in.getInt(in.readerIndex());
        //判断流中是否具备该长度
        if(in.readableBytes() < len + INT_SIZE) {
            //消息体还没全部写入流中，暂时不读取
            return false;
        }
        //流中已经具备完整消息体，可读取
        return true;
    }

    @Override
    public ByteBuf unpack(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if(!this.msgComplete(in)) {
            return null;
        }
        //先读取一次
        int len = in.getInt(in.readerIndex());
        ByteBuf buf = in.readBytes(len + INT_SIZE);
        return buf;
    }

    protected ByteBuf decrypt(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        //当前不做解密处理，当需要解密操作时，重写该函数
        return buf;
    }

    protected abstract T deserialize(ChannelHandlerContext ctx, ByteBuf buf) throws Exception;

    @Override
    public T decode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        //对消息流解密
        buf = this.decrypt(ctx, buf);
        if(buf == null) {
            return null;
        }
        //将消息流反序列化为消息体
        return this.deserialize(ctx, buf);
    }

    protected abstract ByteBuf serialize(ChannelHandlerContext ctx, T msg) throws Exception;

    protected ByteBuf encrypt(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        //当前不做加密处理，当需要加密操作时，重写该函数
        return buf;
    }

    @Override
    public ByteBuf encode(ChannelHandlerContext ctx, T msg) throws Exception {
        //将消息体序列化为消息流
        ByteBuf buf = this.serialize(ctx, msg);
        if(buf == null) {
            return null;
        }
        //对消息流加密
        return this.encrypt(ctx, buf);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exception(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause.getMessage(), cause);
    }
}
