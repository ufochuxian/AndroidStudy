package com.eric.task

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val TAG = "ITask"


const val hasSetPwd = true

// 定义 ITask 接口
interface ITask {
    suspend fun execute(): Boolean
}

// 定义具体任务类
class PermissionTask : ITask {
    override suspend fun execute(): Boolean {
        return withContext(Dispatchers.Main) {
            // 模拟显示权限弹窗，并等待用户操作
            // 例如：showPermissionDialog() 返回用户是否授予权限的结果
            Log.i(TAG, "PermissionTask executed")
            true // 返回结果 true 或 false
        }
    }
}

class PasswordTask : ITask {
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

class BroadcastTask : ITask {
    override suspend fun execute(): Boolean {
        return withContext(Dispatchers.IO) {
            // 模拟请求广告弹窗
            delay(3000)
            Log.i(TAG, "BroadcastTask executed")
            true // 返回结果 true 或 false
        }
    }
}

class GestureTask : ITask {
    override suspend fun execute(): Boolean {
        return withContext(Dispatchers.Main) {
            // 模拟显示手势密码弹窗，并等待用户输入
            Log.i(TAG, "GestureTask executed")
            true // 返回结果 true 或 false
        }
    }
}

// 使用协程执行任务链
suspend fun executeTaskChain(tasks: List<ITask>) {
    coroutineScope {
        for (task in tasks) {
            val result = task.execute()
            if (!result) {
                Log.i(TAG, "Task { ${task::class.simpleName} } execute false, continue chain.")
                continue
            }
        }
    }
}

// 使用示例
fun startTasks() {
    GlobalScope.launch(Dispatchers.Main) {
        val tasks = listOf<ITask>(PermissionTask(), PasswordTask(), BroadcastTask(), GestureTask())
        executeTaskChain(tasks)
    }
}
