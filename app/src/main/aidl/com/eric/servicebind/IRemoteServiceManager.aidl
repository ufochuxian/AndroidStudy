// IRemoteServiceManager.aidl
package com.eric.servicebind;

// Declare any non-default types here with import statements


interface IRemoteServiceManager {
    void registerService(String serviceName, IBinder service);
    IBinder getService(String serviceName);
}