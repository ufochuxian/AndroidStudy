package com.eric.dag.task.flow.test

import Task
import TaskManager
import com.eric.dag.task.flow.CircularDependencyException
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

// 模拟异步请求
suspend fun simulateAsyncRequest(delayTime: Long, result: String): String {
    delay(delayTime)
    return result
}

fun main() = runBlocking {
    val taskManager = TaskManager()

    // 定义任务
    val clockInTask = Task("Clock-In Popup", isAsync = true) {
        println("Executing Clock-In Popup Task...")
        simulateAsyncRequest(2000, "Clock-In Success")
    }

    val incentiveTask = Task("Incentive Popup", isAsync = true) { results ->
        val clockInResult = results[0] as String
        println("Executing Incentive Popup Task after $clockInResult...")
        val incentiveResult = simulateAsyncRequest(3000, "Incentive Points: 50")
        println("Incentive Points Notification sent to Clock-In Popup: $incentiveResult")
        incentiveResult
    }

    val guideTask = Task("Guide Popup", isAsync = true) { results ->
        val previousResult = results.firstOrNull() as String?
        println("Executing Guide Popup Task after previous result: $previousResult")
        simulateAsyncRequest(1000, "Guide Displayed")
    }

    // 设置依赖关系
    incentiveTask.addDependency(clockInTask)
    guideTask.addDependency(incentiveTask)

    // 添加失败处理，如果 Clock-In 或 Incentive 失败时，依旧展示 Guide
    val errorHandlingTask = Task("Guide Popup on Failure", isAsync = true) {
        println("Executing Guide Popup Task on failure...")
        simulateAsyncRequest(1000, "Guide Displayed after failure")
    }
    guideTask.addDependency(errorHandlingTask)

    // 注册任务
    taskManager.registerTasks(listOf(clockInTask, incentiveTask, guideTask))

    // 执行任务
    try {
        taskManager.executeTasks().collect { task ->
            println("${task.name} executed with result: ${task.getResult()}")
        }
    } catch (e: CircularDependencyException) {
        println("Error: ${e.message}")
    }
}