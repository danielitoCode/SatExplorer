package com.elitec.satexplorer.feature.visualization.domain.caseuse

import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObject
import com.elitec.satexplorer.feature.visualization.domain.entity.SceneGraph
import com.elitec.satexplorer.feature.visualization.domain.entity.SceneGraphNode

class BuildSceneGraphUseCase {
    operator fun invoke(objects: List<RenderObject>): SceneGraph {
        val nodes = objects.map { obj ->
            SceneGraphNode(renderObject = obj)
        }
        return SceneGraph(nodes)
    }
}