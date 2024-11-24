package com.eric.base.mgr

import android.content.Context

// PermissionPollingManager 类，使用委托

/**
 * PermissionPollingManager
 * 用于管理特殊权限的轮询逻辑
 */
class PermissionPollingManager(private val context: Context) {

    // 存储每种权限对应的轮询管理器
    private val pollingManagers = mutableMapOf<SpecialPermission, PollingManager>()

    // 存储每种权限对应的处理器
    private val pollingHandlers = mutableMapOf(
        SpecialPermission.STORAGE to StoragePermissionHandler(context),
        SpecialPermission.OVERLAY to OverlayPermissionHandler(context),
        SpecialPermission.USAGE_STATS to UsageStatsPermissionHandler(context),
        SpecialPermission.ACCESSIBILITY to AccessibilityPermissionHandler(context)
    )
    /**
     * 启动指定权限的轮询
     * @param permission 特殊权限类型
     * @param interval 轮询间隔（默认 500 毫秒）
     * @param timeout 轮询超时时间（默认 60 秒）
     */
    fun startPolling(permission: SpecialPermission, interval: Long = 500, timeout: Long = 60000) {
        val handler = pollingHandlers[permission]
            ?: throw IllegalArgumentException("Handler for $permission is not defined.")

        // 创建或获取已有的轮询管理器
        val pollingManager = pollingManagers.getOrPut(permission) {
            PollingManager(
                PollingConfig(
                    interval = interval,
                    timeout = timeout,
                    pollingCallBack = object : PollingCallBack {
                        override fun onTick() = handler.onTick(context)
                        override fun onTimeout() = handler.onTimeout(context)
                    }
                )
            )
        }
        pollingManager.start()
    }

    /**
     * 停止指定权限的轮询
     * @param permission 特殊权限类型
     */
    fun stopPolling(permission: SpecialPermission) {
        pollingManagers[permission]?.stop()
        pollingManagers.remove(permission)
    }

    /**
     * 停止所有权限的轮询
     */
    fun stopAllPolling() {
        pollingManagers.values.forEach { it.stop() }
        pollingManagers.clear()
    }
}
