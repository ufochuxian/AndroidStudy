import com.eric.dag.task.flow.CircularDependencyException
import com.eric.dag.task.flow.VisitStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TaskManager {
    val tasks = mutableListOf<Task<*>>()

    fun <T> registerTask(task: Task<T>) {
        tasks.add(task)
    }

    fun checkForCircularDependencies() {
        val visitStatus = mutableMapOf<Task<*>, VisitStatus>()

        fun dfs(task: Task<*>) {
            visitStatus[task] = VisitStatus.IN_PROGRESS
            for (dependency in task.dependencies) {
                when (visitStatus[dependency]) {
                    VisitStatus.IN_PROGRESS -> throw CircularDependencyException("Circular dependency detected at task: ${task.name}")
                    VisitStatus.UNVISITED -> dfs(dependency)
                    else -> {} // 已处理完成，无需处理
                }
            }
            visitStatus[task] = VisitStatus.COMPLETED
        }

        tasks.forEach { task ->
            if (visitStatus[task] == null || visitStatus[task] == VisitStatus.UNVISITED) {
                dfs(task)
            }
        }
    }

    fun executeTasks(): Flow<Task<*>> = flow {
        val executedTasks = mutableSetOf<Task<*>>()

        suspend fun executeTask(task: Task<*>) {
            task.dependencies.forEach { dependency ->
                if (!executedTasks.contains(dependency)) {
                    executeTask(dependency)
                }
            }
            task.execute()
            executedTasks.add(task)
            emit(task)
        }

        tasks.forEach { task ->
            if (!executedTasks.contains(task)) {
                executeTask(task)
            }
        }
    }
}