package com.eric.base.servicebind;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.eric.base.aidl.IRemoteServiceManager;

public class ServiceManagerHelper {
    private static final String TAG = "ServiceManagerHelper";
    private static IRemoteServiceManager sManager;

    public static void bindServiceManager(final Context context) {
        Intent intent = new Intent(context, ServiceManagerService.class);
        context.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // 使用 asInterface 方法将 IBinder 转换为 IRemoteServiceManager
                sManager = IRemoteServiceManager.Stub.asInterface(service);
                Log.i(TAG, "ServiceManagerService connected: " + sManager);
                // 例如，可在此处注册服务：
                try {
                    sManager.registerService("Calculator", new RemoteCalculatorImpl().asBinder());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                sManager = null;
                Log.w(TAG, "ServiceManagerService disconnected");
            }
        }, Context.BIND_AUTO_CREATE);
    }

    public static IRemoteServiceManager getServiceManager() {
        return sManager;
    }
}
