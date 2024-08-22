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
    val taskE = Task("Task E") { results: List<Any?> ->
        val resultB = results[0] as String
        println("Executing Task E with result from Task B: $resultB")
        "Result E"
    }
    val taskF = Task("Task F") { results: List<Any?> ->
        val resultD = results[0] as String
        println("Executing Task F with result from Task D: $resultD")
        "Result F"
    }
    val taskG = Task("Task G") { results: List<Any?> ->
        val resultE = results[0] as String
        val resultF = results[1] as String
        println("Executing Task G with results from Task E: $resultE and Task F: $resultF")
        "Result G"
    }
    val taskH = Task("Task H") { results: List<Any?> ->
        val resultF = results[0] as String
        println("Executing Task H with result from Task F: $resultF")
        "Result H"
    }
    val taskI = Task("Task I") { results: List<Any?> ->
        val resultG = results[0] as String
        val resultH = results[1] as String
        println("Executing Task I with results from Task G: $resultG and Task H: $resultH")
        "Result I"
    }
    val taskJ = Task("Task J") { results: List<Any?> ->
        val resultI = results[0] as String
        println("Executing Task J with result from Task I: $resultI")
        "Result J"
    }

    // 设置依赖关系
    taskB.addDependency(taskA)
    taskC.addDependency(taskA)
    taskD.addDependency(taskB)
    taskD.addDependency(taskC)
    taskE.addDependency(taskB)
    taskF.addDependency(taskD)
    taskG.addDependency(taskE)
    taskG.addDependency(taskF)
    taskH.addDependency(taskF)
    taskI.addDependency(taskG)
    taskI.addDependency(taskH)
    taskJ.addDependency(taskI)

    // 注册任务
    taskManager.registerTask(taskA)
    taskManager.registerTask(taskB)
    taskManager.registerTask(taskC)
    taskManager.registerTask(taskD)
    taskManager.registerTask(taskE)
    taskManager.registerTask(taskF)
    taskManager.registerTask(taskG)
    taskManager.registerTask(taskH)
    taskManager.registerTask(taskI)
    taskManager.registerTask(taskJ)

    try {
        // 执行任务并处理结果
        taskManager.executeTasks().collect { task ->
            println("${task.name} executed with result: ${task.getResult()}")
        }
    } catch (e: CircularDependencyException) {
        println("Error: ${e.message}")
    }

    val graphvizEngine = GraphvizEngine()
    graphvizEngine.generateGraph(taskManager.tasks)
}