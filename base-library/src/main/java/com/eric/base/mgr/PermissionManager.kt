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
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.eric.base.ext.ERIC_TAG
import com.eric.base.ext.isBelowAndroidVersionInclude10
import com.eric.base.ext.isOverAndroidVersionInclude11
import com.eric.base.ext.isOverAndroidVersionInclude6

/**
 * 权限管理工具类
 *
 * @param T 上下文类型，可以是 Activity 或 Fragment
 */
class PermissionManager<T : Any>(private val owner: T) {

    /**
     * 使用轮询器检测权限状态
     * 适用于 Android 11+ 申请 MANAGE_EXTERNAL_STORAGE 权限时，通过轮询实现用户体验优化。
     */
    private val pollingManager by lazy {
        PollingManager(PollingConfig(pollingCallBack = object : PollingCallBack {
            override fun onTick() {
                // 检测 Android 11+ 的文件管理权限状态
                if (isOverAndroidVersionInclude11() && Environment.isExternalStorageManager()) {
                    if (owner is Activity) {
                        // 大于android 11的版本，申请manager_storage的权限，跳转到设置界面，轮询授权，
                        // 获取到权限后，通过 Intent 置顶当前 Activity，顶掉系统设置界面，返回app，提高用户体验
                        // 权限已获取，通过 Intent 返回应用界面
                        val intent = Intent(owner, owner::class.java).apply {
                            flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        }
                        owner.startActivity(intent)
                    }
                }
            }

            override fun onTimeout() {
                Log.d(ERIC_TAG, "轮询超时，未获取权限")
            }
        }))
    }

    /**
     * 权限申请结果回调接口
     */
    interface PermissionCallback {
        fun onPermissionGranted(result: ActivityResult? = null)
        fun onPermissionDenied(result: ActivityResult? = null)
    }

    companion object {
        private const val REQUEST_CODE_MANAGE_STORAGE = 1001 // 文件管理权限请求码
    }

    private var callback: PermissionCallback? = null

    // 用于文件管理权限的 ActivityResultLauncher
    private lateinit var manageStorageLauncher: ActivityResultLauncher<Intent>

    // 用于动态权限请求的 ActivityResultLauncher
    private lateinit var generalPermissionLauncher: ActivityResultLauncher<Array<String>>

    // 动态权限申请的回调
    private var dynamicPermissionCallback: DynamicPermissionCallback? = null

    /**
     * 动态权限申请回调接口
     */
    interface DynamicPermissionCallback {
        fun onPermissionsGranted() // 所有权限均已授予
        fun onPermissionsDenied(deniedPermissions: List<String>) // 返回被拒绝的权限列表
    }

    init {
        initializeLaunchers()
    }

    /**
     * 初始化 ActivityResultLauncher，用于权限申请的注册
     * 这里使用ActivityResultLauncher这种方法，更解耦的处理权限的请求方法
     *
     *
     */
    private fun initializeLaunchers() {
        when (owner) {
            is AppCompatActivity -> {
                manageStorageLauncher = owner.registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    handleManageStorageResult(result)
                }

                generalPermissionLauncher = owner.registerForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    handleGeneralPermissionResult(permissions)
                }
            }

            is Fragment -> {
                manageStorageLauncher = owner.registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    handleManageStorageResult(result)
                }

                generalPermissionLauncher = owner.registerForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    handleGeneralPermissionResult(permissions)
                }
            }

            else -> throw IllegalArgumentException("Owner must be an AppCompatActivity or Fragment.")
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
     *
     *  3. 小于 Android 6（API < 23）：
     *      默认授予文件权限，无需动态申请。
     */
    fun requestStoragePermission(callback: PermissionCallback) {
        this.callback = callback

        when {
            // Android 11 及以上版本，使用 MANAGE_EXTERNAL_STORAGE (android version >= 11)
            isOverAndroidVersionInclude11() -> {
                processOverAndroid11StoragePermission()
            }
            // Android 6 到 10 版本，申请 READ 和 WRITE 权限 (android version >= 6 && android version <= 10)
            isOverAndroidVersionInclude6() && isBelowAndroidVersionInclude10() -> {
                processAndroid6To10StoragePermission(callback)
            }
            else -> {
                // Android 6 以下版本，默认授予权限
                callback.onPermissionGranted()
            }
        }
    }

    private fun processAndroid6To10StoragePermission(callback: PermissionCallback) {
        this.callback = callback
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        generalPermissionLauncher.launch(permissions)
    }

    @SuppressLint("NewApi")
    private fun processOverAndroid11StoragePermission() {
        if (Environment.isExternalStorageManager()) {
            callback?.onPermissionGranted()
        } else {
            // 引导用户到设置中开启权限
            val intent = Intent(
                Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                Uri.parse("package:${(owner as Context).packageName}")
            )
            manageStorageLauncher.launch(intent)

            // 开启轮询检测权限状态
            pollingManager.start()
        }
    }

    /**
     * 处理文件管理权限申请结果
     */
    private fun handleManageStorageResult(result: ActivityResult) {
        if (isOverAndroidVersionInclude11()) {
            if (Environment.isExternalStorageManager()) {
                callback?.onPermissionGranted(result)
                Log.d(ERIC_TAG, "文件管理权限申请成功")
            } else {
                callback?.onPermissionDenied(result)
                Log.d(ERIC_TAG, "文件管理权限申请失败")
            }
            pollingManager.stop()
        }
    }

    /**
     * 动态申请权限
     *
     * @param permissions 要申请的权限列表
     * @param callback 回调接口，通知权限申请结果
     */
    fun requestPermissions(
        permissions: Array<String>,
        callback: DynamicPermissionCallback
    ) {
        // 检查 generalPermissionLauncher 是否已初始化
        if (!::generalPermissionLauncher.isInitialized) {
            throw IllegalStateException(
                "PermissionManager is not properly initialized. " +
                        "Ensure that PermissionManager is initialized with an AppCompatActivity context " +
                        "before calling requestPermissions."
            )
        }
        this.dynamicPermissionCallback = callback
        generalPermissionLauncher.launch(permissions)
    }

    /**
     * 处理动态权限申请结果
     */
    private fun handleGeneralPermissionResult(permissions: Map<String, Boolean>) {
        val deniedPermissions = permissions.filterValues { !it }.keys
        if (deniedPermissions.isEmpty()) {
            dynamicPermissionCallback?.onPermissionsGranted()
        } else {
            dynamicPermissionCallback?.onPermissionsDenied(deniedPermissions.toList())
        }
    }

    /**
     * 检查是否具有存储权限
     *
     * @return 如果当前版本已授予存储权限，返回 true；否则返回 false
     */
    fun hasStoragePermission(): Boolean {
        return when {
            isOverAndroidVersionInclude11() -> Environment.isExternalStorageManager()
            isOverAndroidVersionInclude6() && isBelowAndroidVersionInclude10() -> {
                val context = when (owner) {
                    is Activity -> owner
                    is Fragment -> owner.requireContext()
                    else -> throw IllegalArgumentException("Owner must be an Activity or Fragment.")
                }

                val readGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                val writeGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED

                readGranted && writeGranted
            }
            else -> true // Android 6 以下版本默认授予权限
        }
    }

    /**
     * 检查是否具有指定的权限
     *
     * @param permissions 要检查的权限列表
     * @return 如果所有权限均已授予，返回 true；否则返回 false
     */
    fun hasPermissions(permissions: Array<String>): Boolean {
        val context = when (owner) {
            is Activity -> owner
            is Fragment -> owner.requireContext()
            else -> throw IllegalArgumentException("Owner must be an Activity or Fragment.")
        }

        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
