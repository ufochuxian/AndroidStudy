package com.plugin

import org.objectweb.asm.*

class ThreadConvergenceClassVisitor extends ClassVisitor {

    ThreadConvergenceClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor)
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        return new ThreadConvergenceMethodVisitor(api, mv, name)
    }
}

class ThreadConvergenceMethodVisitor extends MethodVisitor {

    String methodName

    ThreadConvergenceMethodVisitor(int api, MethodVisitor methodVisitor, String methodName) {
        super(api, methodVisitor)
        this.methodName = methodName
    }

    @Override
    void visitTypeInsn(int opcode, String type) {
        if (opcode == Opcodes.NEW && type == 'java/lang/Thread') {
            println "Detected new Thread in $methodName, replacing with TaskManager."
            // 不调用 super.visitTypeInsn 跳过 NEW 指令
        } else {
            super.visitTypeInsn(opcode, type)
        }
    }

    @Override
    void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (owner == 'java/lang/Thread' && name == '<init>') {
            println "Replacing Thread constructor in $methodName with TaskManager.submitTask"

            // 替换为 TaskManager.submitTask
            super.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    'com/plugin/TaskManager',  // 修复包路径
                    'submitTask',
                    '(Ljava/lang/Runnable;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V',
                    false
            )
        } else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        }
    }
}