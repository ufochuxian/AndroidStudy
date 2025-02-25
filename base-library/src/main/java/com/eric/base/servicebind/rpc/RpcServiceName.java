package com.eric.base.servicebind.rpc;


import androidx.annotation.NonNull;

public enum RpcServiceName {
    REMOTE_CALCULATOR("com.eric.base.aidl.IRemoteCalculator");

    private final String name;

    RpcServiceName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
