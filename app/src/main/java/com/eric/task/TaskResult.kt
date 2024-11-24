package com.eric.task

/**
 * 通用任务执行结果类
 */
sealed class TaskResult<T> {

    /**
     * 表示任务执行成功
     *
     * @param data 成功返回的数据
     */
    data class Success<T>(val data: T?) : TaskResult<T>()

    /**
     * 表示任务执行失败
     *
     * @param error 错误信息
     */
    data class Failure<T>(val error: Throwable?) : TaskResult<T>()

    /**
     * 表示任务被取消
     */
    class Cancelled<T>(val reason: T?) : TaskResult<T>()

    /**
     * 表示任务已完成，无需重新执行
     */
    data class HasFinished<T>(val message: String = "Task already finished.") : TaskResult<T>()

}
