package com.eric.task

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.LifecycleOwner
import com.eric.androidstudy.R
import com.eric.base.dialog.DialogPresenter
import com.eric.base.mgr.PermissionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume


const val TAG = "ITask"


const val hasSetPwd = false

/**
 * // 定义 ITask 接口，支持协变
 * interface ITask<out T> {
 *     suspend fun execute(): Boolean
 * }
 *
 * // 修改任务类，使其遵循新的接口定义
 * class BroadcastTask(val viewModel: BroadCastViewModel?) : ITask<BroadCastViewModel?> {
 *     override suspend fun execute(): Boolean {
 *         return withContext(Dispatchers.IO) {
 *             // 模拟请求广告弹窗
 *             viewModel?.resultData = MutableLiveData(BroadCastResult(0, "开始请求广告"))
 *             delay(3000)
 *             Log.i(TAG, "BroadcastTask executed")
 *             viewModel?.resultData = MutableLiveData(BroadCastResult(0, "广告请求成功"))
 *             true // 返回结果 true 或 false
 *         }
 *     }
 * }
 *
 * 为何需要使用协变？（支持传入的是T类型的子类型，返回的一定是子类）
 *
 * 这个错误是因为在使用泛型时，List<ITask<BaseViewModel>> 与 List<ITask<out BaseViewModel?>> 不兼容。问题在于 ITask<BaseViewModel> 期望的是 BaseViewModel 类型，而你传递的任务列表中的 BroadcastTask 是 ITask<BroadCastViewModel?>，这导致了类型不匹配。
 *
 * 要解决这个问题，可以通过协变（covariant）和泛型类型通配符来调整 ITask 接口的定义。具体来说，可以将 ITask 接口定义为协变的，这样 ITask<out T> 可以接受任何 T 的子类型。
 *
 */
// 定义 ITask 接口
interface ITask<out T> {
    var taskName: String
        get() = this::class.simpleName ?: "UnknownTask"
        set(_) {}
    val dependencies: List<Class<out ITask<*>>>? // 依赖的任务列表
    suspend fun execute(): T
    fun isFinished(): Boolean = false// 判断任务是否已完成
}

// 定义具体任务类
class CameraPermissionTask(
    private val context: Context,
    private val viewModel: BaseViewModel?,
    private val permissionMgr: PermissionManager<LifecycleOwner>?
) : ITask<TaskResult<String?>> {

    override val dependencies: List<Class<out ITask<*>>>?
        get() = null

    override suspend fun execute(): TaskResult<String?> {
        return suspendCancellableCoroutine { continuation ->
            permissionMgr?.requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                object : PermissionManager.DynamicPermissionCallback {
                    override fun onPermissionsGranted() {
                        continuation.resume(TaskResult.Success("相机权限授予任务，相机权限已授予"))
                    }

                    override fun onPermissionsDenied(deniedPermissions: List<String>) {
                        continuation.resume(TaskResult.Failure(Exception("相机权限授予任务，相机权限被拒绝：$deniedPermissions")))
                    }
                }
            )

            // 在任务被取消时，取消回调处理逻辑
            continuation.invokeOnCancellation {
                continuation.resume(TaskResult.Cancelled("相机权限授予任务，相机授权任务被取消"))
            }
        }
    }

    override fun isFinished(): Boolean {
        return permissionMgr?.hasPermissions(arrayOf(Manifest.permission.CAMERA)) == true
    }
}

// StoragePermissionTask 改造
class StoragePermissionTask(
    val context: Context,
    private val viewModel: BaseViewModel?,
    private val permissionMgr: PermissionManager<LifecycleOwner>?
) : ITask<TaskResult<String?>> {

    override val dependencies: List<Class<out ITask<*>>>? = null

    override suspend fun execute(): TaskResult<String?> {
        return suspendCancellableCoroutine { continuation ->
            permissionMgr?.requestStoragePermission(object : PermissionManager.PermissionCallback {
                override fun onPermissionGranted(result: ActivityResult?) {
                    continuation.resume(TaskResult.Success("文件读写权限已授予"))
                }

                override fun onPermissionDenied(result: ActivityResult?) {
                    continuation.resume(TaskResult.Failure(Exception("文件读写权限未被授予")))
                }
            })

            continuation.invokeOnCancellation {
                Log.d(TAG, "任务被取消")
                continuation.resume(TaskResult.Cancelled("文件权限任务被取消"))
            }
        }
    }

    override fun isFinished(): Boolean {
        return permissionMgr?.hasStoragePermission() == true
    }
}

// PasswordTask 改造
class PatternPasswordTask(
    private val context: Context? = null,
    private val viewModel: BaseViewModel?,
    override var taskName: String
) : ITask<TaskResult<String?>> {

    override val dependencies: List<Class<out ITask<*>>>? = null

    override suspend fun execute(): TaskResult<String?> {
        return suspendCancellableCoroutine { continuation ->
            if (context == null) {
                continuation.resume(TaskResult.Failure(Exception("密码任务执行失败，没有context")))
            }
            context?.let {
                val dialogPresenter = DialogPresenter(it)
                Log.i(TAG, "开始设置图形密码")
                dialogPresenter.showCustomDialog(R.layout.layout_pattern_dialog, onConfirm = {
                    continuation.resume(TaskResult.Success("密码任务成功，设置成功图形密码"))
                }, onCancel = {
                    continuation.resume(TaskResult.Cancelled("图形密码任务，设置被取消"))
                })
            }
        }
    }
}

// BroadcastTask 改造
class BroadcastTask(
    private val viewModel: BroadCastViewModel?,
    override var taskName: String
) : ITask<TaskResult<String?>> {

    override val dependencies: List<Class<out ITask<*>>>? = null

    override suspend fun execute(): TaskResult<String?> {
        return withContext(Dispatchers.IO) {
            try {
                Log.i(TAG, "开始请求广告")
                viewModel?.resultData?.postValue(BroadCastResult(0, "开始请求广告"))
                delay(3000) // 模拟广告请求耗时
                viewModel?.resultData?.postValue(BroadCastResult(0, "广告请求成功"))
                TaskResult.Success("广告请求成功")
            } catch (e: Exception) {
                Log.e(TAG, "BroadcastTask: 广告请求失败", e)
                TaskResult.Failure(e)
            }
        }
    }
}

// GestureTask 改造
class GestureTask(
    private val viewModel: BaseViewModel?,
    override var taskName: String
) : ITask<TaskResult<String?>> {

    override val dependencies: List<Class<out ITask<*>>>? = null

    override suspend fun execute(): TaskResult<String?> {
        return withContext(Dispatchers.Main) {
            // 模拟用户设置手势密码成功
            TaskResult.Success("手势密码设置成功")
        }
    }
}
