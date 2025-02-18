package com.plugin

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class ThreadPoolMethodVisitor extends AdviceAdapter {

    protected ThreadPoolMethodVisitor(int api, MethodVisitor mv, int access, String name, String descriptor) {
        super(api, mv, access, name, descriptor)
    }

    @Override
    void visitTypeInsn(int opcode, String type) {
        if (opcode == Opcodes.NEW && type == "java/lang/Thread") {
            println "Intercepting new Thread() in method: ${name}"
            super.visitTypeInsn(Opcodes.NEW, "com/eric/base/thread/TaskManager")
            return
        }
        super.visitTypeInsn(opcode, type)
    }

    @Override
    void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (opcode == Opcodes.INVOKESPECIAL && owner == "java/lang/Thread" && name == "<init>") {
            println "Replacing Thread constructor with TaskManager.submitTask()"
            super.visitMethodInsn(Opcodes.INVOKESTATIC, "com/eric/base/thread/TaskManager", "submitTask",
                    "(Ljava/lang/Runnable;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V", false)
            return
        }

        if (opcode == Opcodes.INVOKEVIRTUAL && owner == "java/lang/Thread" && name == "start") {
            println "Intercepted Thread.start(), replacing with TaskManager"
            return
        }

        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
    }
}
