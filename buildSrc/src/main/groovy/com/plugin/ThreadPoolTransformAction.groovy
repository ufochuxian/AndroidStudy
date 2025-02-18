package com.plugin

import org.gradle.api.file.RegularFileProperty
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

import java.nio.file.Files

abstract class ThreadPoolTransformAction implements WorkAction<ThreadPoolTransformParameters> {

    @Override
    void execute() {
        File inputClassFile = parameters.inputFile.get().asFile
        File outputClassFile = parameters.outputFile.get().asFile

        if (!inputClassFile.name.endsWith(".class")) {
            return
        }

        FileInputStream fis = new FileInputStream(inputClassFile)
        ClassReader cr = new ClassReader(fis)
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES)
        ClassVisitor cv = new ThreadPoolClassVisitor(Opcodes.ASM9, cw)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        fis.close()

        Files.copy(inputClassFile.toPath(), outputClassFile.toPath())
        FileOutputStream fos = new FileOutputStream(outputClassFile)
        fos.write(cw.toByteArray())
        fos.close()
    }
}

interface ThreadPoolTransformParameters extends WorkParameters {
    RegularFileProperty getInputFile()
    RegularFileProperty getOutputFile()
}
