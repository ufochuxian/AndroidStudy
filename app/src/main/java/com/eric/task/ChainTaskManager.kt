package com.eric.task

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ChainTaskManager<T> {

    // 使用协程执行任务链
    suspend fun executeTaskChain(tasks: List<ITask<T?>>) {
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
}