package com.eric.kotlin

/**

 * @Author: chen

 * @datetime: 2024/4/7

 * @desc:

 */

class MagicBox<T, R>(private val item: T) {
    //参数是一个lambada，并且返回值是R类型,这也是函数式编程的一种体现
    //可以通过传入不同的函数，来对数据做不同的计算状态
    fun doSomeMagic(lambada: (T) -> R): R {
//        println("doSomething")
        return lambada(item)
    }
}

class Dog(var name :String) {

}

class Cat(val name:String)

fun main() {
    val boxDog = MagicBox<Dog,String>(Dog("dog"))
    boxDog.doSomeMagic {
        it.name.run {
            plus("_Super").apply(::println)
        }
        it.name
    }

    val boxCat = MagicBox<Cat,Unit>(Cat("cat"))
    boxCat.doSomeMagic {
        println("I am a ${it.name},just so hello")
    }
}