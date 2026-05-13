package com.elitec.satexplorer.feature.visualization.data.renderEngine

import com.elitec.satexplorer.feature.visualization.domain.entity.RenderCommand
import com.elitec.satexplorer.feature.visualization.domain.entity.TransformMatrix

class OpenGlRendererEngine {
    fun render(commands: List<RenderCommand>) {
        commands.forEach { command ->
            when (command) {
                is RenderCommand.DrawMesh -> drawMesh(command.objectId)
                is RenderCommand.SetMatrix -> setMatrix(command.matrix)
                is RenderCommand.SetTexture -> bindTexture(command.textureId)
            }
        }
    }

    private fun drawMesh(id: Long) { /* GLES draw calls */ }

    private fun setMatrix(matrix: TransformMatrix) { /* uniform upload */ }

    private fun bindTexture(textureId: String) { /* GPU texture binding */ }
}