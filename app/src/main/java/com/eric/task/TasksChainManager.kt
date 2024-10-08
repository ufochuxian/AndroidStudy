package com.eric.task

import android.util.Log
import kotlinx.coroutines.coroutineScope

class TasksChainManager<T> {

    // 使用协程执行任务链
    suspend fun executeTasks(tasks: List<ITask<T?>>) {
        coroutineScope {
            for (task in tasks) {
                val result = task.execute()
                if (result == null) {
                    Log.i(TAG, "Task { ${task::class.simpleName} } execute false, continue chain.")
                    continue
                }
            }
        }
    }
}