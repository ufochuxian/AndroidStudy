package com.eric.servicebind;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ServiceManagerService extends Service {
    // 实例化全局服务管理器
    private final RemoteServiceManager mManager = new RemoteServiceManager();

    @Override
    public IBinder onBind(Intent intent) {
        return mManager;
    }
}
