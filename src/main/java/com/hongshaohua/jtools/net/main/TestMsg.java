package com.hongshaohua.jtools.net.main;

/**
 * Created by Aska on 2018/2/2.
 */
public class TestMsg extends TestParent {

    private class InTestMsg<T> {
        private int i;
    }

    public class InTestMsgImpl extends InTestMsg<Long> {

    }
}
