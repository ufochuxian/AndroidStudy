package com.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class ThreadPoolTransformPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.plugins.withId("com.android.application") {
            project.android.applicationVariants.all { variant ->
                def variantName = variant.name.capitalize()
                def compileTaskName = "compile${variantName}JavaWithJavac"
                def transformTaskName = "threadPoolTransform${variantName}"

                // 确保 Java 编译任务存在
                if (project.tasks.findByName(compileTaskName)) {
                    // **修正 inputDir 的路径**
                    def inputDirPath = "intermediates/javac/${variant.name}/compile${variantName}JavaWithJavac/classes"

                    // 注册 Transform 任务
                    def transformTask = project.tasks.register(transformTaskName, ThreadPoolTransformTask) {
                        it.inputDir.set(project.layout.buildDirectory.dir(inputDirPath))
                        it.outputDir.set(project.layout.buildDirectory.dir("intermediates/transformed_classes/${variant.name}"))
                    }

                    // 让 Transform 任务在 Java 编译任务之后执行
                    project.tasks.named(compileTaskName).configure {
                        it.finalizedBy(transformTask)
                    }
                } else {
                    project.logger.warn("Task $compileTaskName not found, skipping threadPoolTransform for $variantName")
                }
            }
        }
    }
}
