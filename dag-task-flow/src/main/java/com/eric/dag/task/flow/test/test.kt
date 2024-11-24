import com.eric.dag.task.flow.CircularDependencyException

suspend fun main() {
    val taskManager = TaskManager()

    // 定义任务
    val taskA = Task("Task A") { _: List<Any?> ->
        println("Executing Task A")
        "Result A"
    }
    val taskB = Task("Task B") { results: List<Any?> ->
        val resultA = results[0] as String
        println("Executing Task B with result from Task A: $resultA")
        "Result B"
    }
    val taskC = Task("Task C") { results: List<Any?> ->
        val resultA = results[0] as String
        println("Executing Task C with result from Task A: $resultA")
        "Result C"
    }
    val taskD = Task("Task D") { results: List<Any?> ->
        val resultB = results[0] as String
        val resultC = results[1] as String
        println("Executing Task D with results from Task B: $resultB and Task C: $resultC")
        "Result D"
    }

    // 设置依赖关系
    taskB.addDependency(taskA)
    taskC.addDependency(taskA)
    taskD.addDependency(taskB)

    val tasks = arrayListOf(taskA,taskB,taskC,taskD)

    // 注册任务
    taskManager.registerTasks(tasks)


    try {
        // 执行任务并处理结果
        taskManager.executeTasks().collect { task ->
            println("${task.name} executed with result: ${task.getResult()}")
        }
    } catch (e: CircularDependencyException) {
        println("Error: ${e.message}")
    }

    val graphvizEngine = GraphvizEngine()
    graphvizEngine.generateGraph(taskManager.getTasks())
}