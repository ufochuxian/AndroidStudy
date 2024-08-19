package com.eric.kotlin.delegate

import com.eric.kotlin.Person
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class TimeDelegate<T, V>(private val inValue : T, private val out : V) : ReadWriteProperty<T, V> {

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        return out.apply {
            println(System.currentTimeMillis())
        }
    }

    override fun setValue(thisRef: T, property: KProperty<*>, value: V) {
        when(inValue) {
            is String -> {
                //KProperty 代表这个委托的属性的“发射属性”，可以通过他获取反射属性的名字，类型等信息。
                println("${inValue} ,${property.name} I am String")
            }
            is Boolean ->{
                println("${inValue},${property.name} I am Boolean")
            }
        }
    }

}

fun main() {
   println( DelegateTest.time)

    DelegateTest.time = "30"
}

object DelegateTest{
    var time by TimeDelegate("inTime","outTime")
}