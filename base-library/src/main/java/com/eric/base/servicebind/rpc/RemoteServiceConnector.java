package com.eric.base.servicebind.rpc;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.eric.base.aidl.IRemoteServiceManager;
import com.eric.base.servicebind.ServiceManagerService;

public class RemoteServiceConnector {
    private static final String TAG = "ServiceManagerHelper";

    // 当前进程中绑定到 ServiceManagerService 后获得的 IRemoteServiceManager 代理
    private IRemoteServiceManager mManager;

    /**
     * 定义回调接口，用于在服务绑定成功后通知调用者
     */
    public interface ServiceManagerConnectionCallback {
        void onConnected(IRemoteServiceManager manager) throws RemoteException;
    }

    /**
     * 在当前进程中绑定 ServiceManagerService，并在绑定成功后通过回调返回 IRemoteServiceManager 实例。
     *
     * 绑定操作是异步的，绑定成功后 onServiceConnected() 会被调用，
     * 我们在该回调中通过 AIDL 自动生成的 Stub.asInterface() 将 IBinder 转换为 IRemoteServiceManager 实例。
     *
     * @param context  用于绑定服务的 Context
     * @param callback 服务绑定成功后的回调通知
     */
    @SuppressLint("LogNotTimber")
    public void bindServiceManager(final Context context, final ServiceManagerConnectionCallback callback) {
        Log.d(TAG, "bindServiceManager: 开始绑定 ServiceManagerService");
        Intent intent = new Intent(context, ServiceManagerService.class);
        context.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected: 绑定成功，ComponentName: " + name);
                // 通过 AIDL 生成的 Stub.asInterface() 方法将 IBinder 转换为 IRemoteServiceManager
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
                Log.d(TAG, "onServiceDisconnected: 服务断开连接，ComponentName: " + name);
                mManager = null;
            }
        }, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "bindServiceManager: 绑定请求已发送");
    }
}
