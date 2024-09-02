package com.eric.task

class ParallelTasksManager : ITasksManager {

    override suspend fun <T> executeTasks(tasks: List<ITask<T>>): Map<String, T> {
        val results = mutableMapOf<String,T>()
        val stringITaskMap = tasks.associateBy { it.taskName }
        return results
    }
}