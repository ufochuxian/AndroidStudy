package com.eric.base.thread

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.Future
import android.util.Log
import com.eric.base.logTd
import com.eric.base.logTe
import com.eric.base.logTw
import retrofit2.http.Tag
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.log

object TaskManager {

    // 默认配置
    private var currentConfig: ThreadPoolConfig = ThreadPoolConfig()

    // 共享线程池
    private var executorService: ThreadPoolExecutor? = null

    // 用来存储每个实例的任务列表
    private val taskMap: MutableMap<String, MutableList<Future<*>>> = mutableMapOf()

    /**
     * 提交任务到线程池，并且标记任务所属的页面实例
     */
    fun submitTask(task: Runnable, pageInstance: Any, fileName: String, methodName: String, config: ThreadPoolConfig? = null) {
        // 如果传入了新的配置，更新配置
        if (config != null) {
            currentConfig = config
        }

        val pageTag = getPageInstanceTag(pageInstance)

        // 如果配置有变化，重新创建线程池
        if (executorService == null || executorService!!.isShutdown) {
            executorService = createThreadPool(currentConfig)
        }

        val future = executorService?.submit {
            logThreadStart(pageTag,fileName, methodName)
            task.run()
        }

        // 为任务添加标识，标记任务来自哪个页面实例
        if (!taskMap.containsKey(pageTag)) {
            taskMap[pageTag] = mutableListOf()
        }
        future?.let {
            taskMap[pageTag]?.add(it)
        }
    }

    /**
     * 获取页面实例的唯一标识符（UUID）
     */
    private fun getPageInstanceTag(pageInstance: Any): String {
        val instanceId = pageInstance.hashCode()
        return "$instanceId"
    }

    fun cancelTasksForPageInstance(pageInstance: Any) {
        val pageTag = getPageInstanceTag(pageInstance)

        // 检查是否有任务与该页面实例相关
        val tasks = taskMap[pageTag]
        if (tasks.isNullOrEmpty()) {
            // 如果没有找到相关任务，记录日志
            logTd("TaskManager", "No tasks to cancel for page instance: $pageTag")
            return
        }

        // 记录任务取消前的日志
        logTd("TaskManager", "Attempting to cancel tasks for page instance: $pageTag. Total tasks: ${tasks.size}")

        // 取消任务并记录日志
        tasks.forEach { future ->
            try {
                val wasCancelled = future.cancel(true)
                if (wasCancelled) {
                    logTd("TaskManager", "Successfully cancelled task for page instance: $pageTag")
                } else {
                    logTw("TaskManager", "Failed to cancel task for page instance: $pageTag")
                }
            } catch (e: Exception) {
                // 处理异常并记录日志
                logTe("TaskManager", "Error while cancelling task for page instance: $pageTag,e:${e}")
            }
        }

        // 清除任务列表
        taskMap.remove(pageTag)
        logTd("TaskManager", "Task list cleared for page instance: $pageTag")
    }


    /**
     * 关闭线程池
     */
    fun shutdown() {
        executorService?.shutdown()
    }

    /**
     * 判断线程池是否已销毁
     */
    fun isExecutorServiceShutdown(): Boolean {
        return executorService?.isShutdown == true
    }

    // 创建线程池
    private fun createThreadPool(config: ThreadPoolConfig): ThreadPoolExecutor {
        return ThreadPoolExecutor(
            config.corePoolSize,
            config.maximumPoolSize,
            config.keepAliveTime,
            TimeUnit.SECONDS,
            LinkedBlockingQueue(config.queueCapacity),
            CustomThreadFactory(config.threadNamePrefix) // 使用自定义的 ThreadFactory
        )
    }

    // 记录线程开启的位置
    private fun logThreadStart(pageTag: String,fileName: String, methodName: String) {
        val threadInfo = "Attempting to start tasks for page instance: $pageTag,Thread started at $fileName - $methodName"
        logTd("TaskManager", threadInfo)
    }
}
