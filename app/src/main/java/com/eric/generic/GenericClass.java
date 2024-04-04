package com.eric.generic;

/**
 * @Author: chen
 * @datetime: 2021/8/6
 * @desc:
 */


import java.util.List;

import kotlin.collections.ArrayDeque;

/**
 * 泛型类的定义
 *
 * @param <T>
 */
class GenericClass<T> {

    //可以使用泛型，这样可以在使用这个类的时候，就确定了参数的类型，类型确定放在了编译器
    private List<T> containers = new ArrayDeque<>();

    /**
     * 泛型方法的定义
     *
     * @param t
     */
    public void add(T t) {
        containers.add(t);
    }

    /**
     * 泛型方法的定义
     */
    public List<T> get() {
        return containers;
    }

}
