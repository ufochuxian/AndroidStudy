package com.eric.router_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: chen
 * @datetime: 2021/6/29
 * @desc:
 */
// 对class类可以起作用，  /** Class, interface (including annotation type), or enum declaration */
@Target(ElementType.TYPE)
// 定义生命周期，编译器有效
@Retention(RetentionPolicy.CLASS)
public @interface Router {
    String value();
}
