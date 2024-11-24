package com.eric.task

import android.app.Dialog
import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.LifecycleOwner
import com.eric.base.mgr.PermissionManager
import com.eric.dialog.DialogPresenter
import com.eric.dialog.PermissionDialogCallback
import com.eric.dialog.PermissionInfo
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AppLockPermissionTask(
    val context: Context,
    override val dependencies: List<Class<out ITask<*>>>?,
    private val permissionMgr: PermissionManager<LifecycleOwner>?
) : ITask<TaskResult<String?>> {
    private var mAppLockPermissionDialog: Dialog? = null

    override suspend fun execute(): TaskResult<String?> {
        return suspendCoroutine { continutation ->
            // 定义权限列表，每个权限包括名称、描述和点击时的动作
            val permissions = listOf(
                PermissionInfo(
                    permissionName = "应用使用情况权限",
                    description = "应用需要获取使用情况统计数据的权限。",
                    action = {
                        // 跳转到应用使用情况权限设置页
                        permissionMgr?.requestUsageStatsPermission(object :
                            PermissionManager.PermissionCallback {

                            override fun onPermissionGranted(result: ActivityResult?) {
                                Log.i(TAG, "获取到应用使用情况权限")
                                onGrantedAppLockPermissions(continutation)
                            }

                            override fun onPermissionDenied(result: ActivityResult?) {
                                Log.i(TAG, "应用使用情况权限没有授权")

                            }
                        })
                    }
                ),
                PermissionInfo(
                    permissionName = "悬浮窗权限",
                    description = "应用需要悬浮窗权限以显示重要提示信息。",
                    action = {
                        // 跳转到悬浮窗权限设置页
                        permissionMgr?.requestOverlayPermission(object :
                            PermissionManager.PermissionCallback {

                            override fun onPermissionGranted(result: ActivityResult?) {
                                Log.i(TAG, "获取到应用悬浮权限")
                                onGrantedAppLockPermissions(continutation)
                            }

                            override fun onPermissionDenied(result: ActivityResult?) {
                                Log.i(TAG, "'应用悬浮权限没有授权")

                            }
                        })
                    }
                )
            )

            val dialogPresenter = DialogPresenter(context)

            // 显示加锁主要权限请求弹窗
            mAppLockPermissionDialog =  dialogPresenter.showCustomPermissionDialog(
                permissions = permissions,
                callback = object : PermissionDialogCallback {
                    override fun onPermissionsGranted() {
                        // 所有权限已授予的处理逻辑
                        Log.d("PermissionDialog", "所有权限已授予")
                        TaskResult.Success("AppLock功能需要的权限，授权成功")
                    }

                    override fun onPermissionsDenied(deniedPermissions: List<String>) {
                        // 权限被拒绝的处理逻辑
                        Log.e("PermissionDialog", "被拒绝的权限: $deniedPermissions")
                    }
                }
            )

        }
    }

    private fun onGrantedAppLockPermissions(continuation: Continuation<TaskResult<String?>>) {
        if (hasGrantedAppLockPermission(permissionMgr)) {
            continuation.resume(TaskResult.Success("AppLock功能需要的权限，授权成功"))
            mAppLockPermissionDialog?.dismiss()
        }
    }

    private fun hasGrantedAppLockPermission(permissionMgr: PermissionManager<LifecycleOwner>?): Boolean {
        return permissionMgr?.hasOverlayPermission() == true && permissionMgr.hasUsageStatsPermission()
    }

    override fun isFinished(): Boolean {
        return hasGrantedAppLockPermission(permissionMgr)
    }
}