package com.hongshaohua.jtools.tcp.serializer;

import com.hongshaohua.jtools.common.reflect.ReflectDataClass;
import com.hongshaohua.jtools.common.reflect.ReflectDataField;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Aska on 2018/2/2.
 *
 * 序列化规则
 * boolean 1 字节
 * byte 1 字节
 * char 2 字节
 * short 2 字节
 * int 4 字节
 * long 8 字节
 * float 4 字节
 * double 8 字节
 *
 *
 * 根据传入的obj的属性顺序，自动进行对象的序列化操作
 * 支持基础类型、String、数组、集合、Map
 *
 *
 *
 */
public class DefaultTcpMsgSerializer {

    private Charset charset;

    private DefaultTcpMsgSerializer(Charset charset) {
        this.charset = charset;
    }

    private boolean writeBoolean(ByteBuf buf, Object object, ReflectDataClass clazz) {
        if(clazz.isBoolean()) {
            buf.writeBoolean((boolean)object);
            return true;
        }
        return false;
    }

    private boolean writeByte(ByteBuf buf, Object object, ReflectDataClass clazz) {
        if(clazz.isByte()) {
            buf.writeByte((byte)object);
            return true;
        }
        return false;
    }

    private boolean writeChar(ByteBuf buf, Object object, ReflectDataClass clazz) {
        if(clazz.isChar()) {
            buf.writeChar((char)object);
            return true;
        }
        return false;
    }

    private boolean writeShort(ByteBuf buf, Object object, ReflectDataClass clazz) {
        if(clazz.isShort()) {
            buf.writeShort((short)object);
            return true;
        }
        return false;
    }

    private boolean writeInt(ByteBuf buf, Object object, ReflectDataClass clazz) {
        if(clazz.isInt()) {
            buf.writeInt((int)object);
            return true;
        }
        return false;
    }

    private boolean writeLong(ByteBuf buf, Object object, ReflectDataClass clazz) {
        if(clazz.isLong()) {
            buf.writeLong((long)object);
            return true;
        }
        return false;
    }

    private boolean writeFloat(ByteBuf buf, Object object, ReflectDataClass clazz) {
        if(clazz.isFloat()) {
            buf.writeFloat((float)object);
            return true;
        }
        return false;
    }

    private boolean writeDouble(ByteBuf buf, Object object, ReflectDataClass clazz) {
        if(clazz.isDouble()) {
            buf.writeDouble((double)object);
            return true;
        }
        return false;
    }

    private boolean writeBasic(ByteBuf buf, Object object, ReflectDataClass clazz) {
        //基础类型不再附加其他数据，直接写入buf
        if(this.writeBoolean(buf, object, clazz)
                || this.writeByte(buf, object, clazz)
                || this.writeChar(buf, object, clazz)
                || this.writeShort(buf, object, clazz)
                || this.writeInt(buf, object, clazz)
                || this.writeLong(buf, object, clazz)
                || this.writeFloat(buf, object, clazz)
                || this.writeDouble(buf, object, clazz)) {
            return true;
        }
        return false;
    }

    private boolean writeString(ByteBuf buf, Object object, ReflectDataClass clazz) {
        if(clazz.isString()) {
            //字符串模式下，如果object为空，len为-1，否则写入字符串长度
            if(object == null) {
                buf.writeInt(-1);
            } else {
                String str = (String)object;
                byte[] bytes = str.getBytes(this.charset);
                buf.writeInt(bytes.length);
                if(bytes.length > 0) {
                    buf.writeBytes(bytes);
                }
            }
            return true;
        }
        return false;
    }

    private void writeClassField(ByteBuf buf, Object object, ReflectDataClass clazz) throws Exception {
        //获取类的所有field，包括父类
        List<ReflectDataField> fields = clazz.getAllFields();
        for(ReflectDataField field : fields) {
            String key = field.getName();
            if(key.equals("$change")) {
                continue;
            }
            Object value = field.getField().get(object);
            this.writeObject(buf, value, field.getClazz());
        }
    }

    private boolean writeClass(ByteBuf buf, Object object, ReflectDataClass clazz) throws Exception {
        //复合类型需要在开头写入该类所有值所占空间长度，如果为null则长度为-1
        if(object == null) {
            buf.writeInt(-1);
            return true;
        }
        //先记录buf当前所在的writeIndex，用来在写完数据后定位len位置与计算object长度
        int index = buf.writerIndex();
        //预先写入长度
        buf.writeInt(0);
        //开始写类数据
        this.writeClassField(buf, object, clazz);
        //计算数据长度
        int endIndex = buf.writerIndex();
        int len = endIndex - Integer.SIZE/8 - index;
        //重新设置长度
        buf.setInt(index, len);
        return true;
    }

    private boolean writeObject(ByteBuf buf, Object object, ReflectDataClass clazz) throws Exception {
        //判断类型
        //先判断基础类型
        if(this.writeBasic(buf, object, clazz)) {
            return true;
        }
        //判断是否为字符串
        if(this.writeString(buf, object, clazz)) {
            return true;
        }
        //判断是否为数组，数组一律规定内部所有
        if(this.writeArray(buf, object, clazz)) {
            return true;
        }
        //判断是否为list
        if(this.writeList(buf, object, clazz)) {
            return true;
        }
        //判断是否为set
        if(this.writeSet(buf, object, clazz)) {
            return true;
        }
        //判断是否为map
        if(this.writeMap(buf, object, clazz)) {
            return true;
        }
        //判断为复合类型
        if(this.writeClass(buf, object, clazz)) {
            return true;
        }
        return false;
    }

    private boolean writeBooleanArray(ByteBuf buf, Object[] array, ReflectDataClass componentClass) {
        if(componentClass.isBoolean()) {
            for(Object value : array) {
                buf.writeBoolean((boolean)value);
            }
            return true;
        }
        return false;
    }

    private boolean writeByteArray(ByteBuf buf, Object[] array, ReflectDataClass componentClass) {
        if(componentClass.isByte()) {
            for(Object value : array) {
                buf.writeByte((byte)value);
            }
            return true;
        }
        return false;
    }

    private boolean writeCharArray(ByteBuf buf, Object[] array, ReflectDataClass componentClass) {
        if(componentClass.isChar()) {
            for(Object value : array) {
                buf.writeChar((char)value);
            }
            return true;
        }
        return false;
    }

    private boolean writeShortArray(ByteBuf buf, Object[] array, ReflectDataClass componentClass) {
        if(componentClass.isShort()) {
            for(Object value : array) {
                buf.writeShort((short)value);
            }
            return true;
        }
        return false;
    }

    private boolean writeIntArray(ByteBuf buf, Object[] array, ReflectDataClass componentClass) {
        if(componentClass.isInt()) {
            for(Object value : array) {
                buf.writeInt((int)value);
            }
            return true;
        }
        return false;
    }

    private boolean writeLongArray(ByteBuf buf, Object[] array, ReflectDataClass componentClass) {
        if(componentClass.isLong()) {
            for(Object value : array) {
                buf.writeLong((long)value);
            }
            return true;
        }
        return false;
    }

    private boolean writeFloatArray(ByteBuf buf, Object[] array, ReflectDataClass componentClass) {
        if(componentClass.isFloat()) {
            for(Object value : array) {
                buf.writeFloat((float)value);
            }
            return true;
        }
        return false;
    }

    private boolean writeDoubleArray(ByteBuf buf, Object[] array, ReflectDataClass componentClass) {
        if(componentClass.isDouble()) {
            for(Object value : array) {
                buf.writeDouble((double)value);
            }
            return true;
        }
        return false;
    }

    private boolean writeObjectArray(ByteBuf buf, Object[] array, ReflectDataClass componentClass) throws Exception {
        for(Object value : array) {
            this.writeObject(buf, value, componentClass);
        }
        return true;
    }

    private boolean writeArray(ByteBuf buf, Object object, ReflectDataClass clazz) throws Exception {
        if(!clazz.isArray()) {
            return false;
        }

        //数组一律先写入数组长度，如果数组为null，则为-1
        if(object == null) {
            buf.writeInt(-1);
            return true;
        }

        Object[] array = (Object[])object;
        buf.writeInt(array.length);
        if(array.length <= 0) {
            return true;
        }

        ReflectDataClass componentClass = clazz.getComponentClass();
        if(this.writeBooleanArray(buf, array, componentClass)
                || this.writeByteArray(buf, array, componentClass)
                || this.writeCharArray(buf, array, componentClass)
                || this.writeShortArray(buf, array, componentClass)
                || this.writeIntArray(buf, array, componentClass)
                || this.writeLongArray(buf, array, componentClass)
                || this.writeFloatArray(buf, array, componentClass)
                || this.writeDoubleArray(buf, array, componentClass)
                || this.writeObjectArray(buf, array, componentClass)) {
            return true;
        }
        return false;
    }

    private boolean writeCollection(ByteBuf buf, Collection collection, ReflectDataClass clazz) throws Exception {
        //将collection转为数组
        Object[] array = collection.toArray();
        //获取第一个模板类作为元素类
        ReflectDataClass componentClass = clazz.getGenericClasses().get(0);

        //数组一律先写入数组长度，如果数组为null，则为-1
        if(array == null) {
            buf.writeInt(-1);
            return true;
        }

        buf.writeInt(array.length);
        if(array.length <= 0) {
            return true;
        }

        if(this.writeBooleanArray(buf, array, componentClass)
                || this.writeByteArray(buf, array, componentClass)
                || this.writeCharArray(buf, array, componentClass)
                || this.writeShortArray(buf, array, componentClass)
                || this.writeIntArray(buf, array, componentClass)
                || this.writeLongArray(buf, array, componentClass)
                || this.writeFloatArray(buf, array, componentClass)
                || this.writeDoubleArray(buf, array, componentClass)
                || this.writeObjectArray(buf, array, componentClass)) {
            return true;
        }
        return false;
    }

    private boolean writeList(ByteBuf buf, Object object, ReflectDataClass clazz) throws Exception {
        if(clazz.isList()
                || clazz.isArrayList()
                || clazz.isLinkedList()) {

            //List一律先写入集合size，如果集合为null，则为-1
            if(object == null) {
                buf.writeInt(-1);
                return true;
            }

            List list = (List)object;
            return this.writeCollection(buf, list, clazz);
        }
        return false;
    }

    private boolean writeSet(ByteBuf buf, Object object, ReflectDataClass clazz) throws Exception {
        if(clazz.isSet()
                || clazz.isHashSet()
                || clazz.isTreeSet()
                || clazz.isLinkedHashSet()) {

            //Set一律先写入集合size，如果集合为null，则为-1
            if(object == null) {
                buf.writeInt(-1);
                return true;
            }

            Set set = (Set)object;
            return this.writeCollection(buf, set, clazz);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private boolean writeMap(ByteBuf buf, Object object, ReflectDataClass clazz) throws Exception {
        if(clazz.isMap()
                || clazz.isHashMap()
                || clazz.isTreeMap()
                || clazz.isLinkedHashMap()
                || clazz.isLinkedHashTreeMap()) {

            //Map一律先写入size，如果Map为null，则为-1
            if(object == null) {
                buf.writeInt(-1);
                return true;
            }

            Map map = (Map)object;
            //map需要先写入Map的size，并将key和value写入
            buf.writeInt(map.size());
            if(map.size() <= 0) {
                return true;
            }
            //获取第一个模板类作为key类，第二个模板为value类
            ReflectDataClass keyClass = clazz.getGenericClasses().get(0);
            ReflectDataClass valueClass = clazz.getGenericClasses().get(1);

            Iterator<Map.Entry> it = map.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry entry = it.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                this.writeObject(buf, key, keyClass);
                this.writeObject(buf, value, valueClass);
            }
            return true;
        }
        return false;
    }


    public static ByteBuf serialize(Object object, ReflectDataClass clazz, Charset charset) throws Exception {
        if(object == null) {
            return null;
        }
        ByteBuf buf = Unpooled.buffer();
        new DefaultTcpMsgSerializer(charset).writeObject(buf, object, clazz);
        return buf;
    }

    public static ByteBuf serialize(Object object, ReflectDataClass clazz) throws Exception {
        return serialize(object, clazz, Charset.defaultCharset());
    }
}
