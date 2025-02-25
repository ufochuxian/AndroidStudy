package com.eric.base.servicebind;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.eric.base.aidl.IRemoteCalculator;
import com.eric.base.aidl.IRemoteServiceManager;

public class ServiceManagerClient {
    private static final String TAG = "ServiceManagerClient";
    // 使用 AIDL 定义的接口类型
    private IRemoteServiceManager mManager;

    public interface ServiceManagerClientCallback {
        void onConnected(IRemoteServiceManager manager) throws RemoteException;
    }

    /**
     * 在客户端进程中绑定 ServiceManagerService，获取 IRemoteServiceManager 实例
     */
    @SuppressLint("LogNotTimber")
    public void bindServiceManager(final Context context, final ServiceManagerClientCallback callback) {
        Log.d(TAG, "bindServiceManager: 开始绑定 ServiceManagerService");
        Intent intent = new Intent(context, ServiceManagerService.class);
        context.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected: 绑定成功，ComponentName: " + name);
                // 通过 AIDL Stub.asInterface() 将 IBinder 转换为 IRemoteServiceManager 实例
                mManager = IRemoteServiceManager.Stub.asInterface(service);
                if (callback != null) {
                    try {
                        callback.onConnected(mManager);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected: 服务断开，ComponentName: " + name);
                mManager = null;
            }
        }, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "bindServiceManager: 绑定请求已发送");
    }
    /**
     * 绑定服务后查询并调用 Calculator 服务
     */
    public void queryCalculatorService(final Context context) {
        bindServiceManager(context, new ServiceManagerClientCallback() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onConnected(IRemoteServiceManager manager) throws RemoteException {
                // 通过服务管理器查询 "Calculator" 服务的 Binder
                IBinder binder = manager.getService("Calculator");
                if (binder != null) {
                    // 获取 AIDL 自动生成的代理对象
                    IRemoteCalculator calculator = IRemoteCalculator.Stub.asInterface(binder);
                    try {
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
