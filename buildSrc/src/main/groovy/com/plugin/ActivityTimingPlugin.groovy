package com.plugin

import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class ActivityTimingPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println "ActivityTimingPlugin: Applying plugin..."

        // 确保应用了 Android Application 插件
        if (!project.plugins.hasPlugin('com.android.application')) {
            throw new IllegalStateException("ActivityTimingPlugin requires the Android application plugin.")
        }
        println "ActivityTimingPlugin: Android application plugin detected."

        // 获取 AndroidComponentsExtension
        def androidComponents = project.extensions.findByType(AndroidComponentsExtension)
        if (androidComponents == null) {
            throw new IllegalStateException("AndroidComponentsExtension not found. Ensure Android plugin is applied.")
        }
        println "ActivityTimingPlugin: AndroidComponentsExtension found."

        // 遍历所有变体并注册任务
        androidComponents.onVariants(androidComponents.selector().all()) { variant ->
            println "ActivityTimingPlugin: Configuring variant: ${variant.name}"

            def taskName = "activityTimingTransform${variant.name.capitalize()}"

            // 注册 ActivityTimingTransform 任务
            def timingTask = project.tasks.register(taskName, ActivityTimingTransform) { task ->
                task.variantName = variant.name

                task.doLast {
                    println "✅ ActivityTimingPlugin: Registered and executed task ${taskName} for variant ${variant.name}"
                }
            }

            // 动态查找 JavaCompile 任务（适用于 Java 和 Kotlin 项目）
            project.tasks.withType(JavaCompile).configureEach { compileTask ->
                if (compileTask.name.toLowerCase().contains(variant.name.toLowerCase())) {
                    compileTask.finalizedBy(timingTask)
                    println "✅ ActivityTimingPlugin: Hooked ${taskName} to ${compileTask.name} for variant ${variant.name}"
                }
            }
        }

        println "✅ ActivityTimingPlugin: Plugin applied successfully."
    }
}
