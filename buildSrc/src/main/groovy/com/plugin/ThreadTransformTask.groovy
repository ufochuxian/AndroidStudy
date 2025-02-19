package com.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.objectweb.asm.*

class ThreadTransformTask extends DefaultTask {

    @TaskAction
    void transform() {
        println "========================"
        println "Running Thread Transform Task..."
        println "========================"

        // 动态确定构建变体目录（如 googleDebug）
        def buildVariant = project.gradle.startParameter.taskNames.find { it.toLowerCase().contains("googledebug") } ? "googleDebug" : "debug"

        // 正确的 class 文件路径
        def classesDir = new File("${project.buildDir}/intermediates/javac/${buildVariant}/compile${buildVariant.capitalize()}JavaWithJavac/classes")

        println "Looking for class files in: ${classesDir.absolutePath}"

        if (classesDir.exists()) {
            classesDir.eachFileRecurse { file ->
                if (file.isFile() && file.name.endsWith(".class")) {
                    println "Found class file: ${file.absolutePath}"
                    transformClassFile(file)
                }
            }
        } else {
            println "No class files found in: ${classesDir.absolutePath}"
        }

        println "========================"
        println "Thread Transform Task Completed"
        println "========================"
    }

    // 负责实际的字节码转换
    public static void transformClassFile(File classFile) {
        println "Processing file: ${classFile.absolutePath}"

        classFile.withInputStream { inputStream ->
            def classReader = new ClassReader(inputStream)
            def classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)

            def classVisitor = new ThreadConvergenceClassVisitor(Opcodes.ASM9, classWriter)
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)

            def modifiedBytes = classWriter.toByteArray()

            // 检查字节码大小变化
            println "Original bytecode size: ${inputStream.available()}"
            println "Modified bytecode size: ${modifiedBytes.length}"

            // 写回修改后的字节码
            classFile.withOutputStream { outputStream ->
                outputStream.write(modifiedBytes)
                println "Successfully transformed and wrote class: ${classFile.name}"
            }
        }
    }
}