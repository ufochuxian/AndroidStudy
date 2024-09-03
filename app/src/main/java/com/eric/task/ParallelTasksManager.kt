package com.eric.task

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge

class ParallelTasksManager : ITasksManager {

    override suspend fun <T> executeTasks(tasks: List<ITask<T>>): Map<String, T> {
        val results = mutableMapOf<String, T>()
        val taskMap = tasks.associateBy { it.taskName }
        val pendingTasks = tasks.toMutableSet()
        while (pendingTasks.isNotEmpty()) {
            val readyTasks = pendingTasks.filter { task ->
                task.dependencies?.all { dep -> results.containsKey(dep.simpleName) } == true
            }
            if (readyTasks.isEmpty()) {
                throw IllegalStateException("Circular dependency detected or missing dependencies.")
            }

            readyTasks.map { task ->
                flow {
                    emit(task.taskName to task.execute())
                }
            }.merge()
                .collect { (taskName, result) ->
                    results[taskName] = result
                    println("Task $taskName executed successfully with result: $result")
                }
            pendingTasks.removeAll(readyTasks)

        }
        return results
    }
}