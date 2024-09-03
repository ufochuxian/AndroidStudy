import com.eric.task.ITask
import com.eric.task.ITasksManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class ParallelTasksManager : ITasksManager {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun <T> executeTasks(tasks: List<ITask<T>>): Map<String, T> {
        val results = mutableMapOf<String, T>()

        // 使用 flatMapMerge 并行执行每个任务
        tasks.asFlow()
            .flatMapMerge { task ->
                flow {
                    val result = withContext(Dispatchers.Default) { task.execute() }
                    emit(task.taskName to result)
                }
            }
            .collect { (taskName, result) ->
                results[taskName] = result
                println("Task $taskName executed successfully with result: $result")
            }

        return results
    }
}
