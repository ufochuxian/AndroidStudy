package com.eric.kotlin

import java.util.Objects

/**

 * @Author: chen

 * @datetime: 2024/4/7

 * @desc:

 */
data class Coordinate(var x: Int, var y: Int) {

    //运算符重载函数，那么就可以直接使用两个对象进行相+啦
    operator fun plus(other:Coordinate) = run {
        //run函数会返回lambada的计算结果，并且会把最后一行作为默认值进行返回
       Coordinate(this.x+other.x,this.y+other.y)
    }

}

//枚举在kotlin中，也是一种类
enum class Direction {
    NORTH, SOUTH, EAST, WEST
}

enum class Level(val value: Int) {
    TOP(0), MIDDLE(1), LOW(2);

    fun showLevel() {
        println(value)
    }
}

class Employee(val name: String, val cardIdNo: String) {

    //因为对象的比较，为了性能的考虑，会首先比较对象的hash值，然后再比较equals方法
    //所以如果重写了对象的equals方法，那么也通常也需要重新定义hashcode方法的规则，这样才可以达到想要的重新定义对象是否相等的规则
    override fun hashCode(): Int {
        return Objects.hash(name, cardIdNo)
    }

    override fun equals(other: Any?): Boolean {
        val otherEmployee = other as Employee
        return this.name == otherEmployee.name && this.cardIdNo == otherEmployee.cardIdNo
    }
}

fun main() {
    //data class，默认就支持“解构”
    val (x, y) = Coordinate(10, 20)
    println("$x,$y")

    val pos1 = Coordinate(200, 100)
    val pos2 = Coordinate(200, 100)

    //data class 相比较与普通的class，它有一些区别，重写了==方法，可以直接比较两个对象的内容。另外对于对象内容的拷贝，可以使用copy方法
    println(pos1 == pos2)

    println(pos1 + pos2)

    val pos3 = Coordinate(10, 20)

    //可以直接使用拷贝内容，这些特性说明data class适合做一些bean的处理
    //另外数据类不能被继承，不能使用open这些关键字
    pos3.copy(pos1.x, pos1.y).run(::println)

    //枚举的构造参数可以有参数，并且还可以在枚举内部定义函数，因为在kotlin中，枚举就是一个类
    Level.MIDDLE.showLevel()

    val employee1 = Employee("jack", "123")
    val employee2 = Employee("jack", "123")
    (employee1 == (employee2)).run(::println)

    val employeesSet = HashSet<Employee>()

    employeesSet.add(employee1)
    employeesSet.add(employee2)

    //这里就会被当作一个对象，添加进去啦
    println(employeesSet)


}