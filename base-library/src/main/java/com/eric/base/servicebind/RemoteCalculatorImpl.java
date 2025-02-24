package com.eric.base.servicebind;

import com.eric.base.aidl.IRemoteCalculator;

public class RemoteCalculatorImpl extends IRemoteCalculator.Stub {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
    
    @Override
    public int subtract(int a, int b) {
        return a - b;
    }
}
