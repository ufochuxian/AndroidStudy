package com.eric.base

import android.Manifest
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.LogUtils
import com.eric.base.mgr.PermissionManager

class AppLockPermissionManager(private val permissionManager: PermissionManager<LifecycleOwner>) {

    fun requestCameraPermission(
        onPermissionGranted: () -> Unit,
        onPermissionDenied: (deniedPermissions: List<String>) -> Unit
    ) {
        permissionManager.requestPermissions(arrayOf(Manifest.permission.CAMERA),
            object : PermissionManager.DynamicPermissionCallback {
                override fun onPermissionsGranted() {
                    LogUtils.d("照相权限授权成功")
                    onPermissionGranted.invoke()
                }
                override fun onPermissionsDenied(deniedPermissions: List<String>) {
                    LogUtils.d("照相权限授权失败")
                    onPermissionDenied.invoke(deniedPermissions)
                }
            })
    }
}

