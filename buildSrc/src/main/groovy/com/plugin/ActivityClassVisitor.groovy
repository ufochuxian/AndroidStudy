package com.plugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class ActivityClassVisitor extends ClassVisitor {

    private String className

    ActivityClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM9, classVisitor)
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        className = name
        super.visit(version, access, name, signature, superName, interfaces)
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (name == "onCreate" || name == "onWindowFocusChanged") {
            return new OnCreateMethodVisitor(access, descriptor, mv, className, name)
        }
        return mv
    }
}
