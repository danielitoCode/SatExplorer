package com.elitec.satexplorer.feature.visualization.domain.entity

data class SceneGraphNode(
    val renderObject: RenderObject,
    val children: List<SceneGraphNode> = emptyList()
)