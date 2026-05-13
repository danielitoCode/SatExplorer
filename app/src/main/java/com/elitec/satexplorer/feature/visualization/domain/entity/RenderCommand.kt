package com.elitec.satexplorer.feature.visualization.domain.entity

sealed class RenderCommand {
    data class DrawMesh(val objectId: Long) : RenderCommand()
    data class SetMatrix(val matrix: TransformMatrix) : RenderCommand()
    data class SetTexture(val textureId: String) : RenderCommand()
}