package com.eric.kotlin

import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

//class Example {
//    var name:String by Delegates.observable("") {_,oldValue,newValue->
//        newValue.toUpperCase()
//        println("姓名从 $oldValue 变为 $newValue")    }
//}
//
//fun main() {
//
//    var p = Example()
//
//    p.name = "john".also(::println)
//
//    p.name = "haha".also(::println)
//
//
//
//}

class Derived1 : ReadWriteProperty<Any?, String> {
    private var storedValue: String = ""

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        println("Get value: $storedValue")
        return storedValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("Set value: $value")
        storedValue = value.toUpperCase()
    }
}

class Example {
    //使用by关键字将name属性，委托给了`Derived对象进行处理`，这样当我们使用`name`属性来进行读取或者设置的时候，实际的操作委托就会给到`Derived1`对象
    var name: String by Derived1()
}

fun main() {
    val example = Example()
    example.name = "John" // Set value: John
    println(example.name) // Get value: JOHN
}
