package com.eric.rxjava;

/**
 * @Author: chen
 * @datetime: 2021/6/25
 * @desc:
 */
interface Function<R, T> {
    R apply(T t);
}
