package com.eric.base

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionManager(private val activity: AppCompatActivity) {

    // 定义接口用于回调
    interface PermissionCallback {
        fun onPermissionGranted()   // 当所有权限被授予
        fun onPermissionDenied(deniedPermissions: List<String>)  // 当有权限被拒绝
    }

    private var permissionCallback: PermissionCallback? = null

    // ActivityResultLauncher，用于处理权限请求结果
    private var permissionLauncher: ActivityResultLauncher<Array<String>> =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val deniedPermissions = permissions.filterValues { !it }.keys.toList()
            if (deniedPermissions.isEmpty()) {
                // 所有权限已授予
                permissionCallback?.onPermissionGranted()
            } else {
                // 存在未授予的权限
                permissionCallback?.onPermissionDenied(deniedPermissions)
            }
        }

    // 请求权限的方法
    fun requestPermissions(permissions: Array<String>, callback: PermissionCallback) {
        this.permissionCallback = callback

        val deniedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isNotEmpty()) {
            // 请求未被授予的权限
            permissionLauncher.launch(deniedPermissions.toTypedArray())
        } else {
            // 所有权限已授予
            callback.onPermissionGranted()
        }
    }

    // 检查单个权限是否已经被授予
    fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }
}
