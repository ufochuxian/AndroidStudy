package com.eric.vault

// 定义一个转换接口，方便后续实现具体的动作
interface TransitionAction {
    fun execute(filePath: String): Boolean
}