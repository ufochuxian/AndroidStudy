package com.eric.servicebind;

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
