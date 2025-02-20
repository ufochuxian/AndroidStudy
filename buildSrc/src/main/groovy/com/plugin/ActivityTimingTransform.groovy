package com.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

class ActivityTimingTransform extends DefaultTask {

    @Input
    String variantName

    @InputDirectory  // ✅ 指定输入目录
    File getInputDir() {
        return project.file("build/intermediates/javac/${variantName}/compile${variantName.capitalize()}JavaWithJavac/classes")
    }

    @OutputDirectory  // ✅ 指定输出目录
    File getOutputDir() {
        return project.file("build/intermediates/transformed_classes/${variantName}")
    }

    @TaskAction
    void transform() {
        println "Running ActivityTimingTransform for variant: $variantName"

        if (!getInputDir().exists()) {
            println "No input classes found in ${getInputDir()}"
            return
        }

        project.fileTree(dir: getInputDir(), includes: ['**/*.class']).each { File classFile ->
            ClassReader classReader = new ClassReader(classFile.bytes)
            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
            ActivityClassVisitor classVisitor = new ActivityClassVisitor(classWriter)
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)

            // 输出修改后的 class 文件
            def outputFile = new File(getOutputDir(), classFile.absolutePath - getInputDir().absolutePath)
            outputFile.parentFile.mkdirs()
            outputFile.bytes = classWriter.toByteArray()
            println "Modified class: ${outputFile.absolutePath}"
        }
    }
}
