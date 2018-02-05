package com.hongshaohua.jtools.net.main;

import com.hongshaohua.jtools.common.reflect.ReflectDataClass;
import com.hongshaohua.jtools.tcp.server.TcpServer;

import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main(String[] args )
    {
        try {
            int boss = 1;
            int work = 4;
            MyHandler handler = new MyHandler();


            TcpServer tcpServer = new TcpServer(boss, work, null);
            tcpServer.bind(10910, handler);

            //Thread.sleep(1000000000);

            //new DefaultAutoW(1);
            //new ReflectDataClass(Test.class);
            //new TestMsgDeserializer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ReflectDataClass clazz = new ReflectDataClass(Test.class);
            Test test = new Test();
            test.setBl(true);
            test.setB((byte)1);
            test.setC((char)2);
            test.setS((short)3);
            test.setI(4);
            test.setL(5);
            test.setF(6);
            test.setD(7);
            test.setStr("str");
            ArrayList<String> strList = new ArrayList<>();
            strList.add("strList1");
            strList.add("strList2");
            test.setStrList(strList);
            test.setStrList(null);

            //ByteBuf buf = DefaultTcpMsgSerializer.serialize(test, clazz);

            //Object tt = DefaultTcpMsgDeserializer.deserialize(buf, clazz);

            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println( "Hello World!" );
    }
}
