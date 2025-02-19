package com.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileTree
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*
import org.gradle.workers.WorkQueue
import org.gradle.workers.WorkerExecutor

import javax.inject.Inject

@CacheableTask
abstract class ThreadPoolTransformTask extends DefaultTask {

    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    final DirectoryProperty inputDir = project.objects.directoryProperty()

    @OutputDirectory
    final DirectoryProperty outputDir = project.objects.directoryProperty()

    @Inject
    abstract WorkerExecutor getWorkerExecutor()

    @TaskAction
    void transformClasses() {
        WorkQueue workQueue = workerExecutor.noIsolation()
        FileTree classFiles = inputDir.get().asFileTree.matching {
            include '**/*.class'
        }

        classFiles.each { File classFile ->
            // **创建新的输出文件**
            File transformedFile = new File(outputDir.get().asFile, classFile.name)

            workQueue.submit(ThreadPoolTransformAction) { parameters ->
                parameters.inputFile.set(classFile)  //  修正
                parameters.outputFile.set(transformedFile)  //  修正
            }
        }
    }
}
