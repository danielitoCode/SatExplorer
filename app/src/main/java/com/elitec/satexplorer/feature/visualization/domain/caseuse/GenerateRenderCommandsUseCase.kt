package com.elitec.satexplorer.feature.visualization.domain.caseuse

import com.elitec.satexplorer.feature.visualization.domain.entity.RenderCommand
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObject
import com.elitec.satexplorer.feature.visualization.domain.entity.SceneGraph
import com.elitec.satexplorer.feature.visualization.domain.entity.SceneGraphNode

class GenerateRenderCommandsUseCase {

    operator fun invoke(scene: SceneGraph): List<RenderCommand> {

        val flatObjects = mutableListOf<RenderObject>()

        fun traverse(node: SceneGraphNode) {
            flatObjects.add(node.renderObject)
            node.children.forEach { traverse(it) }
        }

        scene.rootNodes.forEach { traverse(it) }

        return flatObjects
            .filter { it.isVisible }
            .sortedBy { it.layer }
            .map { RenderCommand.DrawMesh(it.id) }
    }
}