package com.plugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

class ThreadPoolClassVisitor extends ClassVisitor {

    ThreadPoolClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor)
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        return new ThreadPoolMethodVisitor(api, mv, access, name, descriptor)
    }
}
