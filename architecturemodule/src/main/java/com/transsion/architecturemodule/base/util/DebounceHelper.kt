package com.transsion.architecturemodule.base.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


object DebounceHelper {
    private var debounceJob: Job? = null
    fun debounce(
        delayMs: Long = 500,
        parentScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
        action: suspend CoroutineScope.() -> Unit
    ) {
        debounceJob?.cancel()
        debounceJob = parentScope.launch {
            delay(delayMs)
            action()
        }
    }
}
