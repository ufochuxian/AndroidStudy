package com.eric.base.ext

import android.os.Build

// 检查是否为 Android 11 (API 30) 及以上
fun isOverAndroidVersion11() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

// 检查是否为 Android 10 (API 29) 及以上
fun isOverAndroidVersion10() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

// 检查是否为 Android 6.0 (API 23) 及以上
fun isOverAndroidVersion6() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
