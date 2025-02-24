package com.eric.base.servicebind;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.eric.base.aidl.IRemoteServiceManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceManagerService extends Service {
    private final IRemoteServiceManager.Stub mManager = new IRemoteServiceManager.Stub() {
        private final Map<String, IBinder> serviceRegistry = new ConcurrentHashMap<>();

        @Override
        public void registerService(String serviceName, IBinder service) {
            serviceRegistry.put(serviceName, service);
        }

        @Override
        public IBinder getService(String serviceName) {
            return serviceRegistry.get(serviceName);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mManager;
    }
}
