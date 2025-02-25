package com.eric.base.servicebind.rpc;


public enum RpcServiceName {
    REMOTE_CALCULATOR("com.eric.base.aidl.IRemoteCalculator");

    private final String name;

    RpcServiceName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
