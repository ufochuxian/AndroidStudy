import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Task<T>(
    val name: String,
    val isAsync: Boolean = false,
    val action: suspend (List<Any?>) -> T
) {
    val dependencies: MutableList<Task<*>> = mutableListOf()
    val dependents: MutableList<Task<*>> = mutableListOf()
    private var result: T? = null

    fun addDependency(task: Task<*>) {
        dependencies.add(task)
        task.dependents.add(this)
    }

    suspend fun execute(): T {
        val dependencyResults = dependencies.map { it.result }
        result = if (isAsync) {
            withContext(Dispatchers.Default) { action(dependencyResults) }
        } else {
            action(dependencyResults)
        }
        return result!!
    }

    fun getResult(): T? = result
}