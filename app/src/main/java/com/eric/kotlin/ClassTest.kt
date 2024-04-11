package com.eric.kotlin

import java.util.Locale

/**

 * @Author: chen

 * @datetime: 2024/4/6

 * @desc:

 */
class Person {

    //lateinit不能够修饰基础类型（Int,Double,Float,Boolean），并且只能够修饰“不可空”类型
    lateinit var age : String

    val config by lazy {
        loadConfig()
    }

    private fun loadConfig() :String {
        return "localConfig"
    }

    //kotlin中，每一个可变(var)属性都会自动生成一个field属性以及getter和setter方法，可以进行覆写
     var name = "yaoming"
         get()  {
             return field.capitalize(Locale.ROOT)
         }
        set(value) {
             field = value.trim()
        }

    var words:String? = "haha"

    fun sayHelloWithAlso() {
        words = words?.also {
             it.toUpperCase().run (::println)
        }
    }

    fun sayHelloWithLet() {
        words = words?.let {
             it.toUpperCase()
        }
    }
}

fun main() {
    val p = Person()
//    println(p.name)

    p.name = "hahah    "
//    println(p.name)

    //这里如果使用的是also函数，那么name是不会改变的（变成大写），因为also函数返回的是“原始接收者”，如果需要返回的是经过lambada计算后的的结果
    //那么可以使用let函数
    p.sayHelloWithAlso()
    println(p.words)

    //如果是用let操作符，那么返回的是lambada计算结果
    p.sayHelloWithLet()
    println(p.words)

    println(p.config)

    p.superHello()



}