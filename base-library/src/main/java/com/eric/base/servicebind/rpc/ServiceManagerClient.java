package com.eric.base.servicebind.rpc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.eric.base.aidl.IRemoteCalculator;
import com.eric.base.aidl.IRemoteServiceManager;

public class ServiceManagerClient {
    private static final String TAG = "ServiceManagerClient";
    // 使用 ServiceManagerHelper 复用绑定 ServiceManagerService 的逻辑
    private final RemoteServiceConnector serviceManagerHelper = new RemoteServiceConnector();

    /**
     * 在客户端进程中绑定 ServiceManagerService，查询 "Calculator" 服务，并调用其 add 方法
     *
     * @param context 用于绑定服务的 Context
     */
    @SuppressLint("LogNotTimber")
    public void queryCalculatorService(final Context context) {
        serviceManagerHelper.bindServiceManager(context, new RemoteServiceConnector.ServiceManagerConnectionCallback() {
            @Override
            public void onConnected(IRemoteServiceManager manager) throws RemoteException {
                Log.d(TAG, "onConnected: 成功绑定 ServiceManagerService");
                // 通过服务管理器查询 "Calculator" 服务的 IBinder
                IBinder binder = manager.getService("Calculator");
                if (binder != null) {
                    // 将 IBinder 转换为 IRemoteCalculator 代理对象
                    IRemoteCalculator calculator = IRemoteCalculator.Stub.asInterface(binder);
                    try {
                        // 调用远程方法
                        int result = calculator.add(5, 3);
                        Log.d(TAG, "Calculator.add(5, 3) = " + result);
                    } catch (RemoteException e) {
                        Log.e(TAG, "调用 Calculator 服务失败", e);
                    }
                } else {
                    Log.w(TAG, "queryCalculatorService: 未找到 Calculator 服务");
                }
            }
        });
    }
}
