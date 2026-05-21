package com.elitec.satexplorer.feature.visualization.domain.caseuse

import android.opengl.Matrix
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderCommand
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObject
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObjectType
import com.elitec.satexplorer.feature.visualization.domain.entity.SceneGraph
import com.elitec.satexplorer.feature.visualization.domain.entity.SceneGraphNode
import com.elitec.satexplorer.feature.visualization.domain.entity.TransformMatrix

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
            .flatMap { obj ->
                listOf(
                    RenderCommand.SetMatrix(buildModelMatrix(obj)),
                    RenderCommand.DrawMesh(
                        objectId = obj.id,
                        objectType = obj.type,
                        tint = colorFor(obj.type)
                    )
                )
            }
    }

    private fun buildModelMatrix(obj: RenderObject): TransformMatrix {
        val matrix = FloatArray(16)
        Matrix.setIdentityM(matrix, 0)
        Matrix.translateM(matrix, 0, obj.position.x.toFloat(), obj.position.y.toFloat(), obj.position.z.toFloat())
        Matrix.rotateM(matrix, 0, obj.rotation.x.toFloat(), 1f, 0f, 0f)
        Matrix.rotateM(matrix, 0, obj.rotation.y.toFloat(), 0f, 1f, 0f)
        Matrix.rotateM(matrix, 0, obj.rotation.z.toFloat(), 0f, 0f, 1f)
        Matrix.scaleM(matrix, 0, obj.scale.x.toFloat(), obj.scale.y.toFloat(), obj.scale.z.toFloat())
        return TransformMatrix(matrix)
    }

    private fun colorFor(type: RenderObjectType): FloatArray = when (type) {
        RenderObjectType.GLOBE -> floatArrayOf(0.16f, 0.45f, 0.82f, 1f)
        RenderObjectType.SATELLITE -> floatArrayOf(0.95f, 0.95f, 0.95f, 1f)
        RenderObjectType.ORBIT_PATH -> floatArrayOf(0.0f, 217f / 255f, 1.0f, 1.0f)
        RenderObjectType.UI_MARKER -> floatArrayOf(0.2f, 1.0f, 0.55f, 1f)
        else -> floatArrayOf(0.7f, 0.7f, 0.7f, 1f)
    }
}
