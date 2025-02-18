package com.eric.base.thread

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class CustomThreadFactory(private val threadNamePrefix: String) : ThreadFactory {
    private val counter = AtomicInteger(0)

    override fun newThread(r: Runnable): Thread {
        return Thread(r, "$threadNamePrefix-${counter.incrementAndGet()}")
    }
}
