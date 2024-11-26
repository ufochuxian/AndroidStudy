package com.eric.performance

import android.content.Context
import android.os.BatteryManager
import android.util.Log

object BatteryMonitor {

    private var initialBatteryLevel: Float = -1f
    private var currentBatteryLevel: Float = -1f

    // 初始化电量监控（比如在 Application 类的 onCreate 方法中调用）
    fun startMonitoring(context: Context) {
        initialBatteryLevel = getBatteryLevel(context)
        currentBatteryLevel = initialBatteryLevel
        Log.d("BatteryMonitor", "Battery monitoring started: $initialBatteryLevel")
    }

    // 获取当前电量百分比
    private fun getBatteryLevel(context: Context): Float {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batteryLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        return batteryLevel.toFloat()
    }

    // 记录每个页面或者模块的开始
    fun recordStart(context: Context, tag: String) {
        currentBatteryLevel = getBatteryLevel(context)
        Log.d("BatteryMonitor", "Monitoring started for $tag, battery level: $currentBatteryLevel")
    }

    // 记录每个页面或者模块的结束
    fun recordEnd(context: Context, tag: String) {
        val endBatteryLevel = getBatteryLevel(context)
        val batteryConsumed = currentBatteryLevel - endBatteryLevel
        Log.d("BatteryMonitor", "Monitoring ended for $tag, battery consumed: $batteryConsumed%")
        currentBatteryLevel = endBatteryLevel
    }
}
