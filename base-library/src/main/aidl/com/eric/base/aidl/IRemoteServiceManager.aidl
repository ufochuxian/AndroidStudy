// IRemoteServiceManager.aidl
package com.eric.base.aidl;

// Declare any non-default types here with import statements

// IRemoteServiceManager.aidl

import android.os.IBinder;

interface IRemoteServiceManager {
    void registerService(String serviceName, IBinder service);
    IBinder getService(String serviceName);
}
