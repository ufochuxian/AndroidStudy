package com.eric.base.servicebind.rpc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.eric.base.aidl.IRemoteServiceManager;
import com.eric.base.servicebind.RemoteCalculatorImpl;

public class CalculatorServiceProvider {
    private static final String TAG = "CalculatorServiceProvider";
    private final RemoteServiceConnector serviceManagerHelper = new RemoteServiceConnector();

    /**
     * 在服务提供进程中调用此方法（例如在 Service 的 onCreate() 中），绑定 ServiceManagerService，
     * 绑定成功后注册 Calculator 服务。
     */
    public void registerCalculatorService(final Context context) {
        serviceManagerHelper.bindServiceManager(context, new RemoteServiceConnector.ServiceManagerConnectionCallback() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onConnected(IRemoteServiceManager manager) {
                // 绑定成功后，创建服务实现对象并注册
                RemoteCalculatorImpl calculatorImpl = new RemoteCalculatorImpl();
                IBinder calculatorBinder = calculatorImpl.asBinder();
                try {
                    manager.registerService("Calculator", calculatorBinder);
                    Log.d(TAG, "成功注册 Calculator 服务");
                } catch (RemoteException e) {
                    Log.e(TAG, "注册 Calculator 服务失败", e);
                }
            }
        });
    }
}
