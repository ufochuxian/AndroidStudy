package com.eric.base.mgr

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.eric.base.ext.isOverAndroidVersion10
import com.eric.base.ext.isOverAndroidVersion11

class PermissionManager<T : Context>(private val context: T) {

    private val pollingManager by lazy {
        PollingManager(PollingConfig(pollingCallBack = object : PollingCallBack {
            @SuppressLint("NewApi")
            override fun onTick() {
                if (Environment.isExternalStorageManager()) {
                    callback?.onPermissionGranted()
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
        if (isOverAndroidVersion11()) {
            if (Environment.isExternalStorageManager()) {
                callback?.onPermissionGranted(result)
            } else {
                callback?.onPermissionDenied(result)
            }
        }
        pollingManager.stop()
    }

    fun requestStoragePermission(callback: PermissionCallback) {
        this.callback = callback

        when {
            // Android 11 及以上版本，使用 MANAGE_EXTERNAL_STORAGE
            isOverAndroidVersion11() -> {
                processOverAndroid11StoragePermission(callback)
            }
            // Android 6.0 到 Android 10，使用 READ 和 WRITE 权限
            isOverAndroidVersion10() -> {
                processAndroid6To10StoragePermission(callback)
            }

            else -> {
                // Android 6.0 以下，直接授予权限
                callback.onPermissionGranted()
            }
        }
    }

    private fun processAndroid6To10StoragePermission(callback: PermissionCallback) {
        val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        val permissionsNeeded = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(
                context,
                readPermission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsNeeded.add(readPermission)
        }
        if (ContextCompat.checkSelfPermission(
                context,
                writePermission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsNeeded.add(writePermission)
        }

        if (permissionsNeeded.isEmpty()) {
            callback.onPermissionGranted()
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                permissionsNeeded.toTypedArray(),
                REQUEST_CODE_MANAGE_STORAGE
            )
        }
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

    // 权限请求结果处理
    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_MANAGE_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                callback?.onPermissionGranted()
            } else {
                callback?.onPermissionDenied()
            }
        }
    }
}
