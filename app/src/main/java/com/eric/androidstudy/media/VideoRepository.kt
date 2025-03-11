package com.eric.androidstudy.media

class VideoRepository {
    // 这里可以扩展网络视频解析或本地存储
    fun getVideoUrl(inputUrl: String): String {
        return inputUrl // 可以处理URL转换逻辑，如本地缓存/数据库
    }
}
