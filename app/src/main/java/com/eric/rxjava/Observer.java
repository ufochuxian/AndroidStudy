package com.eric.rxjava;

/**
 * @Author: chen
 * @datetime: 2021/6/24
 * @desc:
 */
interface Observer<T> {

    public String name = "default";

    void onNext(T t);

    void onComplete(T t);

    void onError(T t);

}
