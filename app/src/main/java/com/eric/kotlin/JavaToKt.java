package com.eric.kotlin;

import java.io.IOException;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * @Author: chen
 * @datetime: 2024/4/8
 * @desc:
 */
class JavaToKt {

   public void callKt() {
      KotlinTestKt.currentNetWorkTools("okhttp");
   }

   public String sayWelcome() {
      //Java调用kt的方法
      return KotlinTestKt.sayWelcome("haha");
   }

   public void compare() {
      //如果参数是函数的话，可以使用匿名内部类来传入,或者lambda表达式
      KotlinTestKt.compare(1, 2, Object::equals);
   }

   public void callField() {
         Teacher t = new Teacher();
         //Java中没有办法直接调用kt的class的field字段，如果非要直接调用，那么就需要在kt中使用@jvmfield来进行修饰
         t.getAge();
         t.name.getBytes();
   }

   public void showOverloadMethodTest() {
        Teacher t = new Teacher();
        //这里即使在kt中，我们对参数设置了默认值，如果没有使用@JvmOverload强制重载的话，那么这里的参数必须完全匹配，如果加上@JvmOverload那么就不需要啦
//        t.sayHello("hahh");


       //加上@JvmOverload就进行了方法的重载
        t.multyParamsMethod();
   }

   public void showUserExceptionInKt() {
       Teacher t = new Teacher();
       try {
           t.showException();
           //这里在kotlin里面的方法，如果抛出来的异常，必须使用@throws注解进行修饰，在java中调用的时候，才能进行捕获
       }catch (IOException e) {
           System.out.println(e.toString());
       }
   }

   public void showCallLambadaInKt() {
       //对于kotlin中的lambada函数，java定义了Function1...Function23来代表这种类型，后面的数字代表调用的方法的参数的个数
       Function1<String, Unit> lambada = KtToJavaKt.getLambada();
       lambada.invoke("hello");
   }

}
