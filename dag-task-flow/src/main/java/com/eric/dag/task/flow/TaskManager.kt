import com.eric.dag.task.flow.CircularDependencyException
import com.eric.dag.task.flow.VisitStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TaskManager {
    private val tasks = mutableListOf<Task<*>>()

    // 单个任务注册方法
    fun <T> registerTask(task: Task<T>) {
        tasks.add(task)
    }

    // 批量注册任务方法
    fun registerTasks(tasks: List<Task<*>>) {
        this.tasks.addAll(tasks)
    }

    private fun checkForCircularDependencies() {
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
        checkForCircularDependencies() // 在执行任务前检查循环依赖

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

    fun getTasks(): List<Task<*>> {
        return tasks
    }

}
