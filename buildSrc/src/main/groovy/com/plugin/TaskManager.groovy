package com.plugin

import java.util.concurrent.Executors

class TaskManager {

    private static final executorService = Executors.newFixedThreadPool(5)

    static void submitTask(Runnable task, Object pageInstance, String fileName, String methodName) {
        println "Submitting task from $fileName - $methodName"
        executorService.execute(task)
    }
}