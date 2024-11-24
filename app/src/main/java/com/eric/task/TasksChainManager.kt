package com.eric.task

import android.util.Log


class TasksChainManager<T> {

    /**
     * 顺序执行任务链
     * 支持根据前一个任务的结果动态决定是否执行下一个任务
     */
    suspend fun executeTasksSequentially(tasks: List<ITask<TaskResult<T>>>): List<TaskResult<T>> {
        val results = mutableListOf<TaskResult<T>>() // 存储任务结果

        var shouldContinue = true // 控制是否继续执行后续任务
        for (task in tasks) {
            if (!shouldContinue) break // 如果任务链中断，停止执行

            val result = try {
                if (task.isFinished()) {
                    TaskResult.HasFinished(" ${task.taskName} has finished")
                } else {
                    task.execute()
                }
            } catch (e: Exception) {
                TaskResult.Failure(e) // 捕获任务执行中的异常
            }

            results.add(result) // 保存当前任务的结果

            // 根据任务结果决定是否继续执行
            shouldContinue = handleTaskResult(task, result)
        }

        return results
    }

    /**
     * 处理任务结果并决定是否继续执行后续任务
     * @param task 当前任务
     * @param result 当前任务的执行结果
     * @return 是否继续执行后续任务
     */
    private fun handleTaskResult(
        task: ITask<TaskResult<T>>,
        result: TaskResult<T>
    ): Boolean {
        return when (result) {
            is TaskResult.HasFinished -> {
                Log.i(TAG, "Task ${task.taskName} has finished")
                true
            }
            is TaskResult.Success -> {
                Log.i(TAG, "Task ${task.taskName} succeeded: ${result.data}")
                true // 成功则继续执行下一个任务
            }

            is TaskResult.Failure -> {
                Log.e(TAG, "Task ${task.taskName} failed: ${result.error?.message}")
                false // 失败则中断任务链
            }

            is TaskResult.Cancelled -> {
                Log.w(TAG, "Task ${task.taskName} cancelled: ${result.reason}")
                false // 被取消则中断任务链
            }


        }
    }
}

