package com.eric.kotlin.reflect

import com.eric.kotlin.printWithDate
import kotlin.reflect.KCallable
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible

/**

 * @Author: chen

 * @datetime: 2024/7/20

 * @desc:

 */
class TaskActivity(private var tName: String = "default", var id: Int = -1) {
    private var taskName = tName
    private var taskId = id

    override fun toString(): String {
        return "taskName:${taskName},taskId:${taskId}"
    }
}


fun showKClass() {
    val kClass = TaskActivity::class

    println("通过反射获取的第一构造函数")
    println(kClass.primaryConstructor)
    println("通过反射获取的第一构造函数")

    println("通过反射获取的成员属性")
    kClass.members.forEach {
        println(it.name)
    }
    println("通过反射获取的成员属性")

    println("通过反射生成对象，并且修改私有属性")
//    kClass.members.filter {
//        it.name == "taskName"
//    }.first().apply {
//        isAccessible = true
//    }

    //获取对象
    val taskActivity = kClass.primaryConstructor?.call("initial taskName", 1)
    taskActivity.let { it ->
//        println(it)
        //通过反射获取到的对象，获取属性列表，获取指定属性
        kClass.declaredMemberProperties.first {
            it.name == "taskName"
        }.apply {
            // 设置私有属性可见性，修改属性值
            println("修改私有属性之前")
            println(it)
            isAccessible = true
            // 检查属性是否为可变属性（因为kotlin中有var和val两种属性修饰类型，val定义后，不可修改，所以这里需要检查）
            if (this is kotlin.reflect.KMutableProperty<*>) {
                //通过属性的getter和setter方法操作属性
                this.setter.call(it,"modify taskName")
            }
        }.apply {
            println("修改私有属性之后")
            println(it)
        }
    }



    println("通过反射生成对象，并且修改私有属性")


}

fun main() {
    showKClass()

}