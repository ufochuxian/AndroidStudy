// IRemoteCalculator.aidl
package com.eric.base.aidl;

// 引入自定义的 CalculationResult
import com.eric.base.aidl.CalculationResult;

interface IRemoteCalculator {
    CalculationResult add(int a, int b);
    CalculationResult subtract(int a, int b);
}
