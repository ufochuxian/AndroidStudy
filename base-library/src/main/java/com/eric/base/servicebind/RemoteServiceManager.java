package com.eric.base.servicebind;

import android.os.IBinder;

import com.eric.base.aidl.IRemoteServiceManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteServiceManager extends IRemoteServiceManager.Stub {
    private final Map<String, IBinder> serviceRegistry = new ConcurrentHashMap<>();

    @Override
    public void registerService(String serviceName, IBinder service) {
        serviceRegistry.put(serviceName, service);
    }

    @Override
    public IBinder getService(String serviceName) {
        return serviceRegistry.get(serviceName);
    }
}
