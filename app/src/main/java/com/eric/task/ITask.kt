package com.eric.task

import android.content.Context
import android.util.Log
import com.eric.base.mgr.PermissionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import android.Manifest
import androidx.lifecycle.LifecycleOwner


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
    val dependencies: List<Class<out ITask<*>>>? // 依赖的任务列表
    suspend fun execute(): T
}

// 定义具体任务类
class PermissionTask(val context: Context, private val viewModel: BaseViewModel?, override var taskName : String,val permissionMgr: PermissionManager<LifecycleOwner>?) : ITask<Boolean> {
    override val dependencies: List<Class<out ITask<*>>>?
        get() = null
    override suspend fun execute(): Boolean {
        var result = false
        permissionMgr?.requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            object : PermissionManager.DynamicPermissionCallback {
                override fun onPermissionsGranted() {
                    Log.d("PermissionManager", "所有权限已授予")
                    result = true
                }

                override fun onPermissionsDenied(deniedPermissions: List<String>) {
                    Log.d("PermissionManager", "被拒绝的权限：$deniedPermissions")
                    result = false
                }
            }
        )
        return result


//        return withContext(Dispatchers.Main) {
//            // 模拟显示权限弹窗，并等待用户操作
//            // 例如：showPermissionDialog() 返回用户是否授予权限的结果
//            Log.i(TAG, "PermissionTask executed")
//            true // 返回结果 true 或 false
//        }
    }
}

class PasswordTask(private val viewModel: BaseViewModel?,override var taskName : String) : ITask<Boolean> {
    override val dependencies: List<Class<out ITask<*>>>?
        get() = null
    override suspend fun execute(): Boolean {
        return withContext(Dispatchers.Main) {
            if (!hasSetPwd) {
                // 模拟显示数字密码弹窗，并等待用户输入
                Log.i(TAG, "PasswordTask executed")
                return@withContext true // 返回结果 true 或 false
            }
            false
        }
    }
}

class BroadcastTask(private val viewModel: BroadCastViewModel?,override var taskName : String) : ITask<Boolean?> {
    override val dependencies: List<Class<out ITask<*>>>?
        get() = null
    override suspend fun execute(): Boolean {
        return withContext(Dispatchers.IO) {
            // 模拟请求广告弹窗
            Log.i(TAG, "BroadcastTask start execute")
            //在io线程等非主线程进行计算处理后，结果如果需要回调到主线程，需要通过postValue（本质还是通过handler）回调到主线程进行ui更新
            viewModel?.resultData?.postValue(BroadCastResult(0, "开始请求广告"))
            delay(3000)
            Log.i(TAG, "BroadcastTask executed")
            viewModel?.resultData?.postValue(BroadCastResult(0, "请求广告成功"))
            true // 返回结果 true 或 false
        }
    }
}

class GestureTask(private val viewModel: BaseViewModel?,override var taskName : String) : ITask<Boolean> {
    override val dependencies: List<Class<out ITask<*>>>?
        get() = null
    override suspend fun execute(): Boolean {
        return withContext(Dispatchers.Main) {
            // 模拟显示手势密码弹窗，并等待用户输入
            Log.i(TAG, "GestureTask executed")
            true // 返回结果 true 或 false
        }
    }
}