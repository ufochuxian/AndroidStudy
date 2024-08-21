import com.eric.dag.task.flow.CircularDependencyException
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine
import guru.nidi.graphviz.model.Factory
import guru.nidi.graphviz.model.Graph
import guru.nidi.graphviz.model.LinkSource
import guru.nidi.graphviz.model.MutableGraph
import guru.nidi.graphviz.model.MutableNode
import guru.nidi.graphviz.model.Node
import java.io.File

suspend fun main() {
    val taskManager = TaskManager()

    // 定义任务
    val taskA = Task("Task A") { _: List<Any?> ->
        println("Executing Task A")
        42
    }
    val taskB = Task("Task B", isAsync = true) { results: List<Any?> ->
        val resultA = results[0] as Int
        println("Executing Task B asynchronously with result from Task A: $resultA")
        resultA * 2
    }
    val taskC = Task("Task C") { results: List<Any?> ->
        val resultB = results[0] as Int
        println("Executing Task C with result from Task B: $resultB")
        resultB + 10
    }
    val taskD = Task("Task D", isAsync = true) { _: List<Any?> ->
        println("Executing Task D asynchronously")
        "Task D completed"
    }
    val taskE = Task("Task E") { results: List<Any?> ->
        val resultC = results[0] as Int
        val resultD = results[1] as String
        println("Executing Task E with results from Task C: $resultC and Task D: $resultD")
        resultC.toString() + resultD
    }

    // 设置依赖关系
    taskB.addDependency(taskA)
    taskC.addDependency(taskB)
    taskD.addDependency(taskB)
    taskE.addDependency(taskC)
    taskE.addDependency(taskD)

    // 注册任务
    taskManager.registerTask(taskA)
    taskManager.registerTask(taskB)
    taskManager.registerTask(taskC)
    taskManager.registerTask(taskD)
    taskManager.registerTask(taskE)

    try {
        // 先检测循环依赖
        taskManager.checkForCircularDependencies()

        // 执行任务并处理结果
        taskManager.executeTasks().collect { task ->
            println("${task.name} executed with result: ${task.getResult()}")
        }
    } catch (e: CircularDependencyException) {
        println("Error: ${e.message}")
    }

    Graphviz.useEngine(GraphvizCmdLineEngine("/opt/homebrew/Cellar/graphviz/12.1.0/bin/dot"))
    // 生成 DAG 图
    val graph = generateGraph(taskManager.tasks)
    Graphviz.fromGraph(graph).render(Format.PNG).toFile(File("enhanced_dag_with_manager.png"))
}

fun generateGraph(tasks: List<Task<*>>): MutableGraph {
    val nodes: Map<Task<*>, Node> = tasks.associateWith { Factory.node(it.name) }
    val links = mutableListOf<LinkSource>()

    tasks.forEach { task ->
        task.dependencies.forEach { dependency ->
            nodes[dependency]?.let { depNode ->
                nodes[task]?.let { taskNode ->
                    links.add(depNode.linkTo(taskNode).asLinkSource())
                }
            }
        }
    }
    return Factory.mutGraph().add(links)
}