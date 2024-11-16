package com.eric.base.mgr

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.eric.base.ext.ERIC_TAG
import com.eric.base.ext.isBelowAndroidVersionInclude10
import com.eric.base.ext.isOverAndroidVersionInclude11
import com.eric.base.ext.isOverAndroidVersionInclude6

class PermissionManager<T : Context>(private val context: T) {

    private val pollingManager by lazy {
        PollingManager(PollingConfig(pollingCallBack = object : PollingCallBack {
            override fun onTick() {
                if (isOverAndroidVersionInclude11() && Environment.isExternalStorageManager()) {
                    if (context is Activity) {
                        // 大于android 11的版本，申请manager_storage的权限，跳转到设置界面，轮询授权，
                        // 获取到权限后，通过 Intent 置顶当前 Activity，顶掉系统设置界面，返回app，提高用户体验
                        val intent = Intent(context, context::class.java).apply {
                            flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        }
                        context.startActivity(intent)
                    }
                }
            }

            override fun onTimeout() {
            }
        }))
    }

    interface PermissionCallback {
        fun onPermissionGranted(result: ActivityResult? = null)
        fun onPermissionDenied(result: ActivityResult? = null)
    }

    companion object {
        private const val REQUEST_CODE_MANAGE_STORAGE = 1001
    }

    private var callback: PermissionCallback? = null

    // Activity Result API 注册获取权限结果
    private val manageStorageLauncher = (context as? AppCompatActivity)?.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (isOverAndroidVersionInclude11()) {
            if (Environment.isExternalStorageManager()) {
                callback?.onPermissionGranted(result)
                Log.d(ERIC_TAG, "申请权限申请结果成功")
            } else {
                callback?.onPermissionDenied(result)
                Log.d(ERIC_TAG, "申请权限申请结果失败")
            }
            pollingManager.stop()
            Log.d(ERIC_TAG, "权限申请到结果，停止轮询操作")
        }
    }

    /**
     * 文件访问权限版本逻辑
     *  1. 大于 Android 11（API 30+）：
     *      使用 MANAGE_EXTERNAL_STORAGE 申请完整文件管理权限。
     *      如果未授予权限，引导用户跳转设置页面。
     *
     *  2. Android 6（包含） 到 10(包含)（API >=23  <=29）：
     *      动态申请 READ_EXTERNAL_STORAGE 和 WRITE_EXTERNAL_STORAGE 权限。
     *      小于 Android 6（API < 23）：
     *
     *  3. 小于 Android 6（不包含6）（API < 23）：
     *      默认授予文件权限，无需动态申请。
     *
     */

    fun requestStoragePermission(callback: PermissionCallback) {
        this.callback = callback

        when {
            // Android 11 及以上版本，使用 MANAGE_EXTERNAL_STORAGE
            isOverAndroidVersionInclude11() -> {
                processOverAndroid11StoragePermission(callback)
            }
            // 大于Android 10，使用 READ 和 WRITE 权限
            isOverAndroidVersionInclude6() && isBelowAndroidVersionInclude10() -> {
                processAndroid6To10StoragePermission(callback)
            }

            else -> {
                // Android 6.0 以下，直接授予权限
                callback.onPermissionGranted()
            }
        }
    }

    //这里使用ActivityResultLauncher这种方法，更解耦的处理权限的请求方法
    private val permissionLauncher = (context as? AppCompatActivity)?.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            callback?.onPermissionGranted()
        } else {
            callback?.onPermissionDenied()
        }
    }

    private fun processAndroid6To10StoragePermission(callback: PermissionCallback) {
        this.callback = callback
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        permissionLauncher?.launch(permissions)
    }

    @SuppressLint("NewApi")
    private fun processOverAndroid11StoragePermission(callback: PermissionCallback) {
        if (Environment.isExternalStorageManager()) {
            callback.onPermissionGranted()
        } else {
            // 引导用户到设置中开启权限
            val intent = Intent(
                Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                Uri.parse("package:${context.packageName}")
            )
            manageStorageLauncher?.launch(intent)

            //开启自动检测权限task，轮训授权结果，增加用户体验
            pollingManager.start()
        }
    }
}
