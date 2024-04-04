package com.eric.operatprs;

import java.io.Serializable;

/**
 * @Author: chen
 * @datetime: 2021/10/18
 * @desc:
 */
class Person implements Serializable {

    public String uid = "";
    public String userName = "";
    public String telNumber = "";

    @Override
    public String toString() {
        return "Person{" +
                "uid='" + uid + '\'' +
                ", userName='" + userName + '\'' +
                ", telNumber='" + telNumber + '\'' +
                '}';
    }
}
