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
import com.eric.base.ext.isBelowAndroidVersionInclude10
import com.eric.base.ext.isOverAndroidVersionInclude11
import com.eric.base.ext.isOverAndroidVersionInclude6

/**
 * 权限管理工具类
 *
 * @param T 上下文类型，可以是 Activity 或 Fragment
 */
private const val TAG = "PermissionManager"

@Suppress("DEPRECATION")
class PermissionManager<T : Any>(private val owner: T) {

    companion object {


        /**
         * 检查是否具有指定的动态申请的通用权限
         *
         * @param permissions 要检查的权限列表
         * @return 如果所有权限均已授予，返回 true；否则返回 false
         */
        fun hasPermissions(context: Context,permissions: Array<String>): Boolean {
            return permissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        }

        /**
         * 检查是否具有辅助功能权限
         */
        fun hasAccessibilityPermission(context: Context): Boolean {
            val accessibilityManager =
                context.getSystemService(Context.ACCESSIBILITY_SERVICE) as android.view.accessibility.AccessibilityManager
            val enabledServices = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            val packageName = context.packageName
            return enabledServices?.split(":")?.any { it.contains(packageName) } == true &&
                    accessibilityManager.isEnabled
        }

        /**
         * 检查是否具有悬浮窗权限
         */
        fun hasOverlayPermission(context: Context): Boolean {
            return Settings.canDrawOverlays(context)
        }


        /**
         * 检查是否具有存储权限
         *
         * @return 如果当前版本已授予存储权限，返回 true；否则返回 false
         */
        fun hasStoragePermission(context: Context): Boolean {
            return when {
                isOverAndroidVersionInclude11() -> Environment.isExternalStorageManager()
                isOverAndroidVersionInclude6() && isBelowAndroidVersionInclude10() -> {
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
         * 检查是否具有应用使用情况权限
         */
        fun hasUsageStatsPermission(context: Context): Boolean {
            val appOpsManager =
                context.getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
            val mode = appOpsManager.checkOpNoThrow(
                android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
            return mode == android.app.AppOpsManager.MODE_ALLOWED
        }

        /**
         * 检查是否具有定位权限
         */
        fun hasLocationPermission(context: Context): Boolean {
            val fineLocationGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            val coarseLocationGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            return fineLocationGranted && coarseLocationGranted
        }
    }


    /**
     * 使用轮询器检测权限状态
     * 适用于 Android 11+ 申请 MANAGE_EXTERNAL_STORAGE 权限时，通过轮询实现用户体验优化。
     */
    private val pollingManager = PermissionPollingManager(getContext())

    // 启动对应权限轮询
    private fun startPollingForPermission(permission: SpecialPermission) {
        pollingManager.startPolling(permission)
    }

    // 停止对应权限轮询
    private fun stopPollingForPermission(permission: SpecialPermission) {
        pollingManager.stopPolling(permission)
    }

    // 停止所有权限轮询
    fun stopAllPolling() {
        pollingManager.stopAllPolling()
    }

    /**
     * 权限申请结果回调接口
     */
    interface PermissionCallback {
        fun onPermissionGranted(result: ActivityResult? = null)
        fun onPermissionDenied(result: ActivityResult? = null)
    }

    /**
     * 动态权限申请回调接口
     */
    interface DynamicPermissionCallback {
        fun onPermissionsGranted() // 所有权限均已授予
        fun onPermissionsDenied(deniedPermissions: List<String>) // 返回被拒绝的权限列表
    }

    private var callback: PermissionCallback? = null
    private var dynamicPermissionCallback: DynamicPermissionCallback? = null

    // 用于文件管理权限的 ActivityResultLauncher
    private lateinit var manageStorageLauncher: ActivityResultLauncher<Intent>

    // 用于动态权限请求的 ActivityResultLauncher
    private lateinit var generalPermissionLauncher: ActivityResultLauncher<Array<String>>

    // 用于特殊权限（如电池优化、辅助功能权限等）的 ActivityResultLauncher
    private lateinit var specificPermissionLauncher: ActivityResultLauncher<Intent>

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

                specificPermissionLauncher = owner.registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    handleSpecificPermissionResult(result)
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

                specificPermissionLauncher = owner.registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    handleSpecificPermissionResult(result)
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
        currentSpecialPermission = SpecialPermission.STORAGE
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
            currentSpecialPermission = SpecialPermission.STORAGE
            // 引导用户到设置中开启权限
            val intent = Intent(
                Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                Uri.parse("package:${(owner as Context).packageName}")
            )
            manageStorageLauncher.launch(intent)

            // 开启轮询检测权限状态
            startPollingForPermission(SpecialPermission.STORAGE)
        }
    }

    /**
     * 处理文件管理权限申请结果
     */
    private fun handleManageStorageResult(result: ActivityResult) {
        if (isOverAndroidVersionInclude11()) {
            if (Environment.isExternalStorageManager()) {
                callback?.onPermissionGranted(result)
                Log.d(TAG, "文件管理权限申请成功")
            } else {
                callback?.onPermissionDenied(result)
                Log.d(TAG, "文件管理权限申请失败")
            }
            stopPollingForPermission(SpecialPermission.STORAGE)
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

    // 新增逻辑部分


    fun requestUsageStatsPermission(callback: PermissionCallback) {
        if (hasUsageStatsPermission(getContext())) {
            callback.onPermissionGranted()
        } else {
            currentSpecialPermission = SpecialPermission.USAGE_STATS
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            specificPermissionLauncher.launch(intent)
            this.callback = callback
            startPollingForPermission(SpecialPermission.USAGE_STATS)
        }
    }

    /**
     * 请求悬浮窗权限
     */
    fun requestOverlayPermission(callback: PermissionCallback) {
        if (hasOverlayPermission(getContext())) {
            callback.onPermissionGranted()
        } else {
            currentSpecialPermission = SpecialPermission.OVERLAY
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${getContext().packageName}")
            )
            specificPermissionLauncher.launch(intent)
            this.callback = callback
            startPollingForPermission(SpecialPermission.OVERLAY)
        }
    }

    /**
     * 请求电池优化权限
     */
    fun requestBatteryOptimizationPermission(callback: PermissionCallback) {
        val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        specificPermissionLauncher.launch(intent)
        this.callback = callback
    }

    /**
     * 请求辅助功能权限
     */
    fun requestAccessibilityPermission(callback: PermissionCallback) {
        if (hasAccessibilityPermission(getContext())) {
            callback.onPermissionGranted()
        } else {
            currentSpecialPermission = SpecialPermission.ACCESSIBILITY
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            specificPermissionLauncher.launch(intent)
            this.callback = callback
            startPollingForPermission(SpecialPermission.ACCESSIBILITY)
        }
    }



    private fun getContext(): Context {
        return when (owner) {
            is Activity -> owner
            is Fragment -> owner.requireContext()
            else -> throw IllegalArgumentException("Owner must be an Activity or Fragment.")
        }
    }


    // 以下是一些特殊权限

    private var currentSpecialPermission: SpecialPermission? = null

    /**
     * 处理特殊权限申请结果
     */
    private fun handleSpecificPermissionResult(result: ActivityResult) {
        val context = getContext()

        currentSpecialPermission?.let { permission ->
            when (permission) {
                SpecialPermission.USAGE_STATS -> {
                    if (hasUsageStatsPermission(context)) {
                        callback?.onPermissionGranted(result)
                        Log.d(TAG, "应用使用情况权限申请成功")
                    } else {
                        callback?.onPermissionDenied(result)
                        Log.d(TAG, "应用使用情况权限申请失败")
                    }
                    stopPollingForPermission(SpecialPermission.USAGE_STATS)
                }

                SpecialPermission.OVERLAY -> {
                    if (hasOverlayPermission(context)) {
                        callback?.onPermissionGranted(result)
                        Log.d(TAG, "悬浮窗权限申请成功")
                    } else {
                        callback?.onPermissionDenied(result)
                        Log.d(TAG, "悬浮窗权限申请失败")
                    }
                    stopPollingForPermission(SpecialPermission.OVERLAY)
                }

                SpecialPermission.ACCESSIBILITY -> {
                    if (hasAccessibilityPermission(context)) {
                        callback?.onPermissionGranted(result)
                        Log.d(TAG, "辅助功能权限申请成功")
                    } else {
                        callback?.onPermissionDenied(result)
                        Log.d(TAG, "辅助功能权限申请失败")
                    }
                    stopPollingForPermission(SpecialPermission.ACCESSIBILITY)
                }

                SpecialPermission.STORAGE -> {
                    if (hasStoragePermission(context)) {
                        callback?.onPermissionGranted(result)
                        Log.d(TAG, "文件管理权限申请成功")
                    } else {
                        callback?.onPermissionDenied(result)
                        Log.d(TAG, "文件管理权限申请失败")
                    }
                    stopPollingForPermission(SpecialPermission.STORAGE)
                }
            }
        } ?: run {
            Log.w(TAG, "未找到当前请求的特殊权限类型")
            callback?.onPermissionDenied(result)
        }

        // 重置当前权限状态
        currentSpecialPermission = null
    }



}