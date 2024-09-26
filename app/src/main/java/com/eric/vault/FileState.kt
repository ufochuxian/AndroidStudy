package com.eric.vault

// 定义文件状态
sealed class FileState {
    object NORMAL : FileState()   // 正常状态
    object HIDE : FileState()     // 隐藏状态
    object DELETE : FileState()   // 删除状态
}
