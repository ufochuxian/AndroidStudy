package com.eric.dag.task.flow

import Task
import guru.nidi.graphviz.model.MutableGraph

interface IGraphEngine {
    fun generateGraph(tasks: List<Task<*>>)
}