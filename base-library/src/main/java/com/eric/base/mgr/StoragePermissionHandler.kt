package com.eric.base.mgr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log

private const val TAG = "StoragePermissionHandler"

class StoragePermissionHandler(context: Context) : SpecialPermissionPollingHandler {
    override fun onTick(context: Context) {
        // 检查文件管理权限是否已经授予
        if (PermissionManager.hasStoragePermission(context)) {
            Log.d(TAG, "文件管理权限已授予")
            // 如果权限已授予，则通过 Intent 返回应用界面，提升用户体验
            if (context is Activity) {
                // 大于android 11的版本，申请manager_storage的权限，跳转到设置界面，轮询授权，
                // 获取到权限后，通过 Intent 置顶当前 Activity，顶掉系统设置界面，返回app，提高用户体验
                // 权限已获取，通过 Intent 返回应用界面
                clearTopActivity(context)
            }
        }
    }

    override fun onTimeout(context: Context) {
        // 超时时记录日志
        Log.d(TAG, "文件管理权限轮询超时")
    }
}


class OverlayPermissionHandler(context: Context) : SpecialPermissionPollingHandler {
    override fun onTick(context: Context) {
        if (PermissionManager.hasOverlayPermission(context)) {
            Log.d(TAG, "悬浮窗权限已授予")
            clearTopActivity(context)
        }
    }

    override fun onTimeout(context: Context) {
        Log.d(TAG, "悬浮窗权限轮询超时")
    }
}

class UsageStatsPermissionHandler(context: Context) : SpecialPermissionPollingHandler {
    override fun onTick(context: Context) {
        if (PermissionManager.hasUsageStatsPermission(context)) {
            Log.d(TAG, "应用使用情况权限已授予")
            clearTopActivity(context)
        }
    }

    override fun onTimeout(context: Context) {
        Log.d(TAG, "应用使用情况权限轮询超时")
    }
}

class AccessibilityPermissionHandler(context: Context) : SpecialPermissionPollingHandler {
    override fun onTick(context: Context) {
        if (PermissionManager.hasAccessibilityPermission(context)) {
            Log.d(TAG, "辅助功能权限已授予")
        }
    }

    override fun onTimeout(context: Context) {
        Log.d(TAG, "辅助功能权限轮询超时")
    }
}

private fun clearTopActivity(context: Context) {
    val intent = Intent(context, context::class.java).apply {
        flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
    }
    context.startActivity(intent)
}