package com.eric.base.servicebind;

import com.eric.base.aidl.CalculationResult;
import com.eric.base.aidl.IRemoteCalculator;

public class RemoteCalculatorImpl extends IRemoteCalculator.Stub {
    @Override
    public CalculationResult add(int a, int b) {
        int sum = a + b;
        return new CalculationResult(sum, "Computed " + a + " + " + b);
    }

    @Override
    public CalculationResult subtract(int a, int b) {
        int diff = a - b;
        return new CalculationResult(diff, "Computed " + a + " - " + b);
    }
}