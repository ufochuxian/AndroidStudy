package com.eric.kotlin

import java.util.Locale

/**

 * @Author: chen

 * @datetime: 2024/4/4

 * @desc:

 */

//这是一个匿名函数
val connectFun :(s1:String,s2:String) -> String = { s1, s2 ->
    s1.plus(s2)
}


fun main() {
    val value = 5
    println(value)

    /* in关键字 */
    val age = 19
    if(age in 0..18) {
        println("未成年")
    } else {
        println("成年人")
    }

    //when关键字
    val shoesLogo = "nike"

    when(shoesLogo) {
        "nike" -> println("这是耐克鞋子")
        "lining"-> println("这是李宁鞋子")
        else -> println("这我还不认识的品牌")
    }

    // for关键字

    for(x in 1..10) {
        if(x == 9) {
            continue
        } else {
            println(x)
        }
    }


    //`` 用来处理一些关键字的命名，不好调用的问题
    Test.`is`()

    sayHello("yaoming")

    //匿名函数
    println(connectFun("hello "," world"))

    //具名函数
    println(detailConnectFunction("a","b","c"))

    //使用lambada表达式的地方，也可以使用函数引用
    println(compare(2,2, ::connect))

    //自带的一些lambada
    val list = listOf("a","b","c","d")

    val jointedString = list.fold("initial") { acc: String, s: String ->
        //acc = acc + plus //注意这里不能写成这样，这样会认为返回值为unit,而这里的Lambada需要的返回值是String
        acc.plus(s)
    }
    println(jointedString)

    //省略括号的lambada表示法
    run { println("这是只有一个参数，并且参数还是lambada的形式的时候，可以完全省略圆括号") }

    //如果不省略的话
    run ({ println("这是只有一个参数，并且参数还是lambada的形式的时候，可以完全省略圆括号") })

    //高阶函数，使用的一些例子
    val names = listOf("yaoming","ataisite","damahou","li","haha","a")
    val newNames = names.filterNot { it.length > 4 }.sortedBy { it }.map { it.toUpperCase() }
    println(names)
    println(newNames)

    //空合并操作符

    var isNullString = null
    var x = isNullString?:"butterflay"
    println(isNullString)

    //字符串相关操作

    val strs = "'jack' is playing basketball"

    val name = strs.substring(strs.indexOf("\'") + 1 until strs.lastIndexOf("\'") )
    println(name)

    //解构操作 这里的使用圆括号
    val domainString = "prefix,domain,name"
    val (prfix,domain,n) = domainString.split(",")
    println("$prfix,$domain,$n")

    //字符串,正则表达式，匹配，并可以跟一个lambda表达式，进行相应的替换
    val tips = "this is a top car"
    val newTips = tips.replace(Regex("[top]")) {
        when(it.value) {
            "t" -> "l"
            "o" -> "o"
            "p" -> "w"
            else -> it.value
        }
    }
    println("newTips:$newTips")


    //比较两个对象
    val name1:String = "jason"
    val name2:String = "jason"

    //因为在java中，字符串存储于字符串常量池，如果两个字符串内容一样的话，那么就意味着他们指向的是常量池的同一个对象，所以相等
    println(name1 === name2)

    val name3 = "JASON"
    val name4 = name2.capitalize(Locale.ROOT)

    //这里尽管name4转换之后,也变成了"JASON",但是由于在java中字符串内容是不可变的，这里如果调用capitalize方法之后
    //实际的操作是在常量池，重新开辟了空间，生成了一个新的包含内容"JASON"的对象，所以和name3是不同的对象
    println(name3 === name4)


}


fun connect(a:Any ,b: Any) :Boolean {
    return  a == b
}


//自定义一个比较任意两个元素的，高阶函数（函数式变成）
//这里是具名函数，所以一定要使用return显示返回，如果是匿名函数，可以不用
fun compare(x1:Any,x2:Any,connect:(a:Any,b:Any)->Boolean):Boolean {
    return connect(x1,x2)
}

fun currentNetWorkTools(type:String) {
    val toolsTemp :(type : String) ->String = {
        "当前正在使用的是${type}的框架"
    }
    toolsTemp(type)
}
