package com.eric.memory

import android.content.Context
import android.os.Debug
import android.os.Environment
import com.eric.base.logTd
import java.io.IOException

fun dumpHprof(context : Context) {
    val filePath = context.filesDir.absolutePath + "/heap_dump.hprof"
    try {
        Debug.dumpHprofData(filePath)
        logTd("dumpHprof","Heap dump saved at: $filePath")
    } catch (e: IOException) {
        e.printStackTrace()
    }
}
