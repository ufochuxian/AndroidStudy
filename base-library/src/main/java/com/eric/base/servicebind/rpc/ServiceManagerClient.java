package com.eric.base.servicebind.rpc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.eric.base.aidl.IRemoteServiceManager;

/**
 * 泛型版的 ServiceManagerClient，支持任意服务的注册和查询。
 */
public class ServiceManagerClient {
    private static final String TAG = "ServiceManagerClient";
    // 复用 RemoteServiceConnector 用于绑定 ServiceManagerService
    private final RemoteServiceConnector remoteServiceConnector = new RemoteServiceConnector();

    /**
     * 注册服务。
     *
     * @param context     用于绑定服务的 Context
     * @param serviceName 服务名称，用于后续查询
     * @param service     服务实现对象（必须继承自 IInterface）
     * @param <T>         服务接口类型
     */
    public <T extends android.os.IInterface> void registerService(final Context context,
                                                                  final String serviceName,
                                                                  final T service) {
        remoteServiceConnector.bindServiceManager(context, new RemoteServiceConnector.ServiceManagerConnectionCallback() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onConnected(IRemoteServiceManager manager) {
                IBinder binder = service.asBinder();
                try {
                    manager.registerService(serviceName, binder);
                    Log.d(TAG, "成功注册服务: " + serviceName);
                } catch (RemoteException e) {
                    Log.e(TAG, "注册服务 " + serviceName + " 失败", e);
                }
            }
        });
    }

    /**
     * 定义一个转换器接口，用于将 IBinder 转换为指定的服务接口对象。
     *
     * @param <T> 服务接口类型
     */
    public interface ServiceConverter<T extends android.os.IInterface> {
        T convert(IBinder binder);
    }

    /**
     * 定义查询结果的回调接口。
     *
     * @param <T> 服务接口类型
     */
    public interface ServiceQueryCallback<T extends android.os.IInterface> {
        void onResult(T service);
    }

    /**
     * 查询服务。
     *
     * @param context     用于绑定服务的 Context
     * @param serviceName 服务名称，用于查询服务
     * @param converter   转换器，将 IBinder 转换为具体服务接口对象
     * @param callback    查询结果的回调
     * @param <T>         服务接口类型
     */
    public <T extends android.os.IInterface> void queryService(final Context context,
                                                               final String serviceName,
                                                               final ServiceConverter<T> converter,
                                                               final ServiceQueryCallback<T> callback) {
        remoteServiceConnector.bindServiceManager(context, new RemoteServiceConnector.ServiceManagerConnectionCallback() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onConnected(IRemoteServiceManager manager) {
                try {
                    IBinder binder = manager.getService(serviceName);
                    if (binder != null) {
                        T service = converter.convert(binder);
                        callback.onResult(service);
                    } else {
                        Log.w(TAG, "未找到服务: " + serviceName);
                        callback.onResult(null);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "查询服务 " + serviceName + " 失败", e);
                    callback.onResult(null);
                }
            }
        });
    }
}
