import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@RequiresApi(Build.VERSION_CODES.O)
fun main() = runBlocking {
    val filePath = "output.txt"
    val file = File(filePath)

    // 如果文件已经存在，先删除它
    if (file.exists()) {
        file.delete()
    }

    // 创建一个 Mutex 实例，用来保证并发安全
    val mutex = Mutex()

    // 使用 flow 生成要写入的数据流
    val dataFlow = (1..1000).asFlow()

    // 启用 10 个协程来并发处理数据写入
    dataFlow
        .buffer()  // 启用背压，以避免生成数据的速度过快
        .chunked(100)  // 将数据分块，以便每个协程可以一次性写入一部分数据
        .map { chunk ->
            // 将数据转换为字符串，每个数据换行
            chunk.joinToString("\n")
        }
        .map { data ->
            // 在这里写入文件，但需要通过 Mutex 进行同步控制
            withContext(Dispatchers.IO) {
                mutex.withLock {
                    Files.write(
                        Paths.get(filePath),
                        "$data\n".toByteArray(),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND
                    )
                }
            }
        }
        .flowOn(Dispatchers.Default)  // 在默认调度器上执行 flow
        .collect()


    println("数据写入完成")
}
