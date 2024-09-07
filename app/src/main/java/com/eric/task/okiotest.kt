package com.eric.task
import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.FileUtils
import okio.*
import java.io.File

fun copyFile(sourceFile: File,context: Context) {
    // 获取应用私有存储目录
    val destinationFile = File(context.getExternalFilesDir(null), sourceFile.name)

    // 检查是否能写入到目标文件
    if (!destinationFile.exists()) {
        try {
            destinationFile.createNewFile()
        } catch (e: Exception) {
            println("Error creating file: ${e.message}")
            return
        }
    }
    // 打开 Source 读取源文件
    val source: Source = sourceFile.source()
    // 打开 Sink 写入目标文件
    val sink: Sink = destinationFile.sink().buffer()

    // 使用 use 确保资源关闭
    source.use { input ->
        sink.use { output ->
            // 定义缓冲区大小（例如8KB）
            val buffer = Buffer()
            var bytesRead: Long
            while (input.read(buffer, 8192).also { bytesRead = it } != -1L) {
                output.write(buffer, bytesRead) // 将读取的数据写入目标文件
            }
        }
    }

    Log.i("","File copied successfully from ${sourceFile.absolutePath} to ${destinationFile.absolutePath}")
}
