import com.eric.dag.task.flow.IGraphEngine
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine
import guru.nidi.graphviz.model.Factory
import guru.nidi.graphviz.model.LinkSource
import guru.nidi.graphviz.model.MutableGraph
import guru.nidi.graphviz.model.Node
import java.io.File

class GraphvizEngine : IGraphEngine {
    init {
        // 指定 Graphviz 可执行文件路径
        Graphviz.useEngine(GraphvizCmdLineEngine("/opt/homebrew/Cellar/graphviz/12.1.0/bin/dot"))
    }

    override fun generateGraph(tasks: List<Task<*>>) {
        // 创建任务节点映射
        val nodes: Map<Task<*>, Node> = tasks.associateWith { Factory.node(it.name) }
        val links = mutableListOf<LinkSource>()

        // 创建任务间的链接
        tasks.forEach { task ->
            task.dependencies.forEach { dependency ->
                nodes[dependency]?.let { depNode ->
                    nodes[task]?.let { taskNode ->
                        // 将链接添加到链接列表中
                        links.add(depNode.linkTo(taskNode).asLinkSource())
                    }
                }
            }
        }

        // 创建可变图，并添加所有链接和节点
        val graph: MutableGraph = Factory.mutGraph().setDirected(true).add(*nodes.values.toTypedArray()).add(links)

        // 渲染图像并保存为 PNG 文件
        Graphviz.fromGraph(graph).render(Format.PNG).toFile(File("enhanced_dag_with_manager.png"))
    }
}
