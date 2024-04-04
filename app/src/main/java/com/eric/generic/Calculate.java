package com.eric.generic;

/**
 * @Author: chen
 * @datetime: 2022/11/25
 * @desc:
 */
public class Calculate<T extends String> {

    public void sayHello(T name) {
        System.out.println("hello" + name);
    }
}
