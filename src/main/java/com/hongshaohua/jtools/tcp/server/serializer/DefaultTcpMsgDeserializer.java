package com.hongshaohua.jtools.tcp.server.serializer;

import com.google.gson.internal.LinkedHashTreeMap;
import com.hongshaohua.jtools.common.reflect.ReflectDataClass;
import com.hongshaohua.jtools.common.reflect.ReflectDataField;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Aska on 2018/2/1.
 *
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
 * 根据传入的obj的属性顺序，自动进行对象的反序列化操作
 * 支持基础类型、String、数组、集合、Map等
 * 注意：集合和Map等泛型无法直接反序列化，必须放入某一类型中并才可
 *
 */
public class DefaultTcpMsgDeserializer {

    private Charset charset;

    private DefaultTcpMsgDeserializer(Charset charset) {
        this.charset = charset;
    }

    private class Result {
        boolean hit = false;
        Object object = null;
    }

    private Object readBoolean(ByteBuf buf, ReflectDataClass clazz) {
        if(clazz.isBoolean()) {
            return buf.readBoolean();
        }
        return null;
    }

    private Object readByte(ByteBuf buf, ReflectDataClass clazz) {
        if(clazz.isByte()) {
            return buf.readByte();
        }
        return null;
    }

    private Object readChar(ByteBuf buf, ReflectDataClass clazz) {
        if(clazz.isChar()) {
            return buf.readChar();
        }
        return null;
    }

    private Object readShort(ByteBuf buf, ReflectDataClass clazz) {
        if(clazz.isShort()) {
            return buf.readShort();
        }
        return null;
    }

    private Object readInt(ByteBuf buf, ReflectDataClass clazz) {
        if(clazz.isInt()) {
            return buf.readInt();
        }
        return null;
    }

    private Object readLong(ByteBuf buf, ReflectDataClass clazz) {
        if(clazz.isLong()) {
            return buf.readLong();
        }
        return null;
    }

    private Object readFloat(ByteBuf buf, ReflectDataClass clazz) {
        if(clazz.isFloat()) {
            return buf.readFloat();
        }
        return null;
    }

    private Object readDouble(ByteBuf buf, ReflectDataClass clazz) {
        if(clazz.isDouble()) {
            return buf.readDouble();
        }
        return null;
    }

    private Object readBasic(ByteBuf buf, ReflectDataClass clazz) {
        Object object = null;

        object = this.readBoolean(buf, clazz);
        if(object != null) {
            return object;
        }

        object = this.readByte(buf, clazz);
        if(object != null) {
            return object;
        }

        object = this.readChar(buf, clazz);
        if(object != null) {
            return object;
        }

        object = this.readShort(buf, clazz);
        if(object != null) {
            return object;
        }

        object = this.readInt(buf, clazz);
        if(object != null) {
            return object;
        }

        object = this.readLong(buf, clazz);
        if(object != null) {
            return object;
        }

        object = this.readFloat(buf, clazz);
        if(object != null) {
            return object;
        }

        object = this.readDouble(buf, clazz);
        if(object != null) {
            return object;
        }

        return null;
    }

    private String readString(ByteBuf buf) {
        int len = buf.readInt();
        if(len < 0) {
            return null;
        } else if(len == 0) {
            return "";
        }
        byte[] bytes = new byte[len];
        buf.readBytes(bytes);
        return new String(bytes, this.charset);
    }

    private Result readString(ByteBuf buf, ReflectDataClass clazz) {
        Result res = new Result();
        if(clazz.isString()) {
            res.hit = true;
            res.object = this.readString(buf);
            return res;
        }
        return res;
    }

    private void readClass(ByteBuf buf, ReflectDataClass clazz, Object object) throws Exception {
        //获取类的所有field，包括父类
        List<ReflectDataField> fields = clazz.getAllFields();
        for(ReflectDataField field : fields) {
            String key = field.getName();
            if(key.equals("$change")) {
                continue;
            }
            field.getField().set(object, this.readObject(buf, field.getClazz()));
        }
    }

    private Object readClass(ByteBuf buf, ReflectDataClass clazz) throws Exception {
        //复合类型需要从开头读取该类所有值所占空间长度，长度为-1则为null
        int len = buf.readInt();
        if(len < 0) {
            return null;
        }
        Object object = clazz.getClazz().newInstance();
        this.readClass(buf, clazz, object);
        return object;
    }

    private Object readObject(ByteBuf buf, ReflectDataClass clazz) throws Exception {
        Object object = null;

        object = this.readBasic(buf, clazz);
        if(object != null) {
            return object;
        }

        Result res = this.readString(buf, clazz);
        if(res.hit) {
            return res.object;
        }

        res = this.readArray(buf, clazz);
        if(res.hit) {
            return res.object;
        }

        res = this.readList(buf, clazz);
        if(res.hit) {
            return res.object;
        }

        res = this.readSet(buf, clazz);
        if(res.hit) {
            return res.object;
        }

        res = this.readMap(buf, clazz);
        if(res.hit) {
            return res.object;
        }

        return this.readClass(buf, clazz);
    }

    private Object readBooleanArray(ByteBuf buf, ReflectDataClass componentClass, int len) {
        if(componentClass.isBoolean()) {
            boolean[] array = new boolean[len];
            for(int i = 0; i < len; i++) {
                array[i] = buf.readBoolean();
            }
            return array;
        }
        return null;
    }

    private Object readByteArray(ByteBuf buf, ReflectDataClass componentClass, int len) {
        if(componentClass.isByte()) {
            byte[] array = new byte[len];
            for(int i = 0; i < len; i++) {
                array[i] = buf.readByte();
            }
            return array;
        }
        return null;
    }

    private Object readCharArray(ByteBuf buf, ReflectDataClass componentClass, int len) {
        if(componentClass.isChar()) {
            char[] array = new char[len];
            for(int i = 0; i < len; i++) {
                array[i] = buf.readChar();
            }
            return array;
        }
        return null;
    }

    private Object readShortArray(ByteBuf buf, ReflectDataClass componentClass, int len) {
        if(componentClass.isShort()) {
            short[] array = new short[len];
            for(int i = 0; i < len; i++) {
                array[i] = buf.readShort();
            }
            return array;
        }
        return null;
    }

    private Object readIntArray(ByteBuf buf, ReflectDataClass componentClass, int len) {
        if(componentClass.isInt()) {
            int[] array = new int[len];
            for(int i = 0; i < len; i++) {
                array[i] = buf.readInt();
            }
            return array;
        }
        return null;
    }

    private Object readLongArray(ByteBuf buf, ReflectDataClass componentClass, int len) {
        if(componentClass.isLong()) {
            long[] array = new long[len];
            for(int i = 0; i < len; i++) {
                array[i] = buf.readLong();
            }
            return array;
        }
        return null;
    }

    private Object readFloatArray(ByteBuf buf, ReflectDataClass componentClass, int len) {
        if(componentClass.isFloat()) {
            float[] array = new float[len];
            for(int i = 0; i < len; i++) {
                array[i] = buf.readFloat();
            }
            return array;
        }
        return null;
    }

    private Object readDoubleArray(ByteBuf buf, ReflectDataClass componentClass, int len) {
        if(componentClass.isDouble()) {
            double[] array = new double[len];
            for(int i = 0; i < len; i++) {
                array[i] = buf.readDouble();
            }
            return array;
        }
        return null;
    }

    private Object readBasicArray(ByteBuf buf, ReflectDataClass componentClass, int len) {
        Object object = null;

        object = this.readBooleanArray(buf, componentClass, len);
        if(object != null) {
            return object;
        }

        object = this.readByteArray(buf, componentClass, len);
        if(object != null) {
            return object;
        }

        object = this.readCharArray(buf, componentClass, len);
        if(object != null) {
            return object;
        }

        object = this.readShortArray(buf, componentClass, len);
        if(object != null) {
            return object;
        }

        object = this.readIntArray(buf, componentClass, len);
        if(object != null) {
            return object;
        }

        object = this.readLongArray(buf, componentClass, len);
        if(object != null) {
            return object;
        }

        object = this.readFloatArray(buf, componentClass, len);
        if(object != null) {
            return object;
        }

        object = this.readDoubleArray(buf, componentClass, len);
        if(object != null) {
            return object;
        }
        return null;
    }

    private Result readStringArray(ByteBuf buf, ReflectDataClass componentClass, int len) {
        Result res = new Result();
        if(componentClass.isString()) {
            res.hit = true;
            String[] array = new String[len];
            for(int i = 0; i < len; i++) {
                array[i] = this.readString(buf);
            }
            res.object = array;
        }
        return res;
    }

    private Object readObjectArray(ByteBuf buf, ReflectDataClass componentClass, int len) throws Exception {
        Object[] array = (Object[]) Array.newInstance(componentClass.getClazz(), len);
        for(int i = 0; i < len; i++) {
            array[i] = this.readObject(buf, componentClass);
        }
        return array;
    }

    private Result readArray(ByteBuf buf, ReflectDataClass clazz) throws Exception {
        Result res = new Result();
        //判断是否为数组
        if(!clazz.isArray()) {
            return res;
        }
        res.hit = true;

        //先读取数组长度
        int len = buf.readInt();
        if(len < 0) {
            return res;
        }

        ReflectDataClass componentClass = clazz.getComponentClass();

        res.object = this.readBasicArray(buf, componentClass, len);
        if(res.object != null) {
            return res;
        }

        Result strRes = this.readStringArray(buf, componentClass, len);
        if(strRes.hit) {
            res.object = strRes.object;
            return res;
        }

        res.object = this.readObjectArray(buf, componentClass, len);
        return res;
    }

    private Result readList(ByteBuf buf, ReflectDataClass clazz) throws Exception {
        Result res = new Result();
        //判断是否为List
        if(clazz.isList()
                || clazz.isArrayList()
                || clazz.isLinkedList()) {
            res.hit = true;

            //先读取List长度，当len为-1时，表示该List为空
            int len = buf.readInt();
            if(len < 0) {
                return res;
            }

            //根据类型创建List
            List list = null;
            if(clazz.isList()) {
                //当List只标明List接口时，默认使用ArrayList
                list = new ArrayList();
            } else if(clazz.isArrayList()) {
                list = new ArrayList();
            } else if(clazz.isLinkedList()) {
                list = new LinkedList();
            }

            //获取List模板
            ReflectDataClass genericClass = clazz.getGenericClasses().get(0);
            for(int i = 0; i < len; i++) {
                list.add(this.readObject(buf, genericClass));
            }
            res.object = list;
        }
        return res;
    }

    private Result readSet(ByteBuf buf, ReflectDataClass clazz) throws Exception {
        Result res = new Result();
        //判断是否为Set
        if(clazz.isSet()
                || clazz.isHashSet()
                || clazz.isTreeSet()
                || clazz.isLinkedHashSet()) {
            res.hit = true;

            //先读取Set长度，当len为-1时，表示该Set为空
            int len = buf.readInt();
            if(len < 0) {
                return res;
            }

            //根据类型创建Set
            Set set = null;
            if(clazz.isSet()) {
                //当Set只标明Set接口时，默认使用HashSet
                set = new HashSet();
            } else if(clazz.isHashSet()) {
                set = new HashSet();
            } else if(clazz.isTreeSet()) {
                set = new TreeSet();
            } else if(clazz.isLinkedHashSet()) {
                set = new LinkedHashSet();
            }

            //获取Set模板
            ReflectDataClass genericClass = clazz.getGenericClasses().get(0);
            for(int i = 0; i < len; i++) {
                set.add(this.readObject(buf, genericClass));
            }
            res.object = set;
        }
        return res;
    }

    private Result readMap(ByteBuf buf, ReflectDataClass clazz) throws Exception {
        Result res = new Result();
        //判断是否为Map
        if(clazz.isMap()
                || clazz.isHashMap()
                || clazz.isTreeMap()
                || clazz.isLinkedHashMap()
                || clazz.isLinkedHashTreeMap()) {
            res.hit = true;

            //先读取Map长度，当len为-1时，表示该Map为空
            int len = buf.readInt();
            if(len < 0) {
                return res;
            }

            //根据类型创建Map
            Map map = null;
            if(clazz.isMap()) {
                //当Map只标明Map接口时，默认使用HashMap
                map = new HashMap();
            } else if(clazz.isHashMap()) {
                map = new HashMap();
            } else if(clazz.isTreeMap()) {
                map = new TreeMap();
            } else if(clazz.isLinkedHashMap()) {
                map = new LinkedHashMap();
            } else if(clazz.isLinkedHashTreeMap()) {
                map = new LinkedHashTreeMap();
            }

            //获取Map模板
            ReflectDataClass genericKeyClass = clazz.getGenericClasses().get(0);
            ReflectDataClass genericValueClass = clazz.getGenericClasses().get(1);
            for(int i = 0; i < len; i++) {
                map.put(this.readObject(buf, genericKeyClass), this.readObject(buf, genericValueClass));
            }
            res.object = map;
        }
        return res;
    }


    /**
     * 根据设置的class属性顺序，自动进行对象的反序列化操作
     * 对象class必须有一个无参数构造函数
     * */
    public static Object deserialize(ByteBuf buf, ReflectDataClass clazz, Charset charset) throws Exception {
        return new DefaultTcpMsgDeserializer(charset).readObject(buf, clazz);
    }

    public static Object deserialize(ByteBuf buf, ReflectDataClass clazz) throws Exception {
        return deserialize(buf, clazz, Charset.defaultCharset());
    }
}
