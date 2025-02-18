package com.eric.base.thread

data class ThreadPoolConfig(
    var corePoolSize: Int = 4,    // 核心线程池大小
    var maximumPoolSize: Int = 8, // 最大线程池大小
    var keepAliveTime: Long = 60, // 线程空闲时间（秒）
    var queueCapacity: Int = 100, // 队列大小
    var threadNamePrefix: String = "CustomThread" // 线程名前缀
)
