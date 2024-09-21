package com.eric.media

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionManager(private val activity: Activity) {

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
        val requiredPermissions = arrayOf(
            Manifest.permission.CAMERA,
        )
    }

    // 检查是否授予了所有需要的权限
    fun hasPermissions(): Boolean {
        return requiredPermissions.all { permission ->
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    // 请求权限
    fun requestPermissions() {
        ActivityCompat.requestPermissions(activity, requiredPermissions, CAMERA_PERMISSION_CODE)
    }

    // 检查权限请求结果
    fun handlePermissionResult(requestCode: Int, grantResults: IntArray, onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }
}
