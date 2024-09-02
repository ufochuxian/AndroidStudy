package com.eric.task

interface ITasksManager {
    suspend fun <T> executeTasks(tasks: List<ITask<T>>): Map<String, T>
}