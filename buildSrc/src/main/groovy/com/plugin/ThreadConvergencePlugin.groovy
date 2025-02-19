package com.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class ThreadConvergencePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        // 注册任务
        def threadConvergenceTask = project.tasks.register("threadConvergence", ThreadTransformTask) {
            group = 'bytecode'
            description = 'Transforms new Thread() to TaskManager.submitTask()'
        }

        project.afterEvaluate {
            // 找到 Java 编译任务
            def compileTask = project.tasks.findByName('compileGoogleDebugJavaWithJavac')
                    ?: project.tasks.findByName('compileDebugJavaWithJavac')

            if (compileTask) {
                // 确保 threadConvergence 在编译之后运行
                threadConvergenceTask.configure {
                    shouldRunAfter compileTask
                }
                project.tasks.named('assembleGoogleDebug').configure {
                    dependsOn threadConvergenceTask
                }
                println "threadConvergence task hooked to ${compileTask.name}."
            } else {
                println "No suitable compile task found to hook threadConvergence."
            }
        }
    }
}
