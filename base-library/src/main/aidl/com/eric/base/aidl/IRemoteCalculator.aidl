// IRemoteCalculator.aidl
package com.eric.base.aidl;

// 引入自定义的 CalculationResult
import com.eric.base.aidl.CalculationResult;
import com.eric.base.aidl.DeviceInfo;

interface IRemoteCalculator {
    //这里如果如参是object类型，需要显示指定
    //ERROR: /Users/chenjianxiang/Documents/opensource/AndroidStudy/base-library/src/main/aidl/com/eric/base/aidl/IRemoteCalculator.aidl:9.37-39: The direction of 'a' is not specified. parcelable/union can be an in, out, or inout parameter.
    CalculationResult get(in DeviceInfo a, int b);
    CalculationResult add(int a, int b);
    CalculationResult subtract(int a, int b);
}
