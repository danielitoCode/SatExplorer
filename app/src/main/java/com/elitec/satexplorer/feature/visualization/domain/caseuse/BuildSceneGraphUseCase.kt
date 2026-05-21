package com.elitec.satexplorer.feature.visualization.domain.caseuse

import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObject
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObjectType
import com.elitec.satexplorer.feature.visualization.domain.entity.SceneGraph
import com.elitec.satexplorer.feature.visualization.domain.entity.SceneGraphNode

class BuildSceneGraphUseCase {

    operator fun invoke(objects: List<RenderObject>): SceneGraph {

        val globe = objects.filter { it.type == RenderObjectType.GLOBE }

        val childObjects = objects.filter { it.type != RenderObjectType.GLOBE }

        val globeNodes = globe.map { g ->
            SceneGraphNode(
                renderObject = g,
                children = childObjects.map { child ->
                    SceneGraphNode(renderObject = child)
                }
            )
        }

        return SceneGraph(globeNodes)
    }
}