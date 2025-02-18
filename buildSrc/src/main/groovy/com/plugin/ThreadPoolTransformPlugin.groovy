package com.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class ThreadPoolTransformPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.plugins.withId("com.android.application") {
            project.android.applicationVariants.all { variant ->
                // 只针对 Debug 变体启用 Transform
                def variantName = variant.name.capitalize()
                def transformTaskName = "threadPoolTransform${variantName}"

                // 注册 Transform 任务
                def transformTask = project.tasks.register(transformTaskName, ThreadPoolTransformTask) {
                    it.inputDir.set(project.layout.buildDirectory.dir("intermediates/javac/${variant.name}/classes"))
                    it.outputDir.set(project.layout.buildDirectory.dir("intermediates/transformed_classes/${variant.name}"))
                }

                // 让 `preBuild` 任务执行 `threadPoolTransform`
                project.tasks.named("pre${variantName}Build").configure {
                    it.dependsOn(transformTask)
                }
            }
        }
    }
}
