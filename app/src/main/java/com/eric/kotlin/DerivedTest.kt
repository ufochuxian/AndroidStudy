package com.eric.kotlin

interface  Base {
    fun print()
}
class BaseImpl(val x: Int) : Base {
    override fun print() { print(x) }
}
// 通过 by关键字将所有 Base，接口中的函数调用委托给 b。
class Derived(b: Base) : Base by b
fun main() {
    val b = BaseImpl(10)
    Derived(b).print()
}


