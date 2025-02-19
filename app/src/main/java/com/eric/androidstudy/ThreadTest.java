package com.eric.androidstudy;

import android.util.Log;

public class ThreadTest {

    public void testThreadMethod() {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

}
