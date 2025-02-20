package com.plugin

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.LocalVariablesSorter

class OnCreateMethodVisitor extends LocalVariablesSorter {

    private String className
    private String methodName

    OnCreateMethodVisitor(int access, String desc, MethodVisitor methodVisitor, String className, String methodName) {
        super(Opcodes.ASM9, access, desc, methodVisitor)
        this.className = className
        this.methodName = methodName
    }

    @Override
    void visitCode() {
        super.visitCode()
        println("OnCreateMethodVisitor visitCode - ${className}.${methodName}")

        // 方法开始时插入时间记录代码
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/os/SystemClock", "elapsedRealtime", "()J", false)
        mv.visitVarInsn(Opcodes.LSTORE, 1)  // 存储开始时间

        // 插入日志（修复 GString 问题）
        mv.visitLdcInsn("ActivityTiming")
        mv.visitLdcInsn("Entering method: ${className}.${methodName}".toString())
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false)
        mv.visitInsn(Opcodes.POP)
    }

    @Override
    void visitInsn(int opcode) {
        if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
            // 计算耗时
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/os/SystemClock", "elapsedRealtime", "()J", false)
            mv.visitVarInsn(Opcodes.LLOAD, 1)
            mv.visitInsn(Opcodes.LSUB)
            mv.visitVarInsn(Opcodes.LSTORE, 3)

            // 日志记录耗时（修复 GString 问题）
            mv.visitLdcInsn("ActivityTiming")
            mv.visitLdcInsn("${className}.${methodName} is returning, took: ".toString())
            mv.visitVarInsn(Opcodes.LLOAD, 3)
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", "(J)Ljava/lang/String;", false)
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false)
            mv.visitInsn(Opcodes.POP)
        }
        super.visitInsn(opcode)
    }
}
