// IRemoteCalculator.aidl
package com.eric.servicebind;

// Declare any non-default types here with import statements

// 声明这个接口可以跨进程调用
interface IRemoteCalculator {
    int add(int a, int b);
    int subtract(int a, int b);
}