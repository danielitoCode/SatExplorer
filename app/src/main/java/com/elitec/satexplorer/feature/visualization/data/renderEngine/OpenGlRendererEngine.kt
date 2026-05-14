package com.elitec.satexplorer.feature.visualization.data.renderEngine

import android.opengl.GLES20
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderCommand
import com.elitec.satexplorer.feature.visualization.domain.entity.TransformMatrix

class OpenGlRendererEngine {

    private var currentMatrix: TransformMatrix? = null

    fun init() {

        // =========================
        // OPENGL STATE
        // =========================

        GLES20.glEnable(
            GLES20.GL_DEPTH_TEST
        )

        GLES20.glEnable(
            GLES20.GL_CULL_FACE
        )

        GLES20.glCullFace(
            GLES20.GL_BACK
        )

        GLES20.glFrontFace(
            GLES20.GL_CCW
        )

        GLES20.glEnable(
            GLES20.GL_BLEND
        )

        GLES20.glBlendFunc(
            GLES20.GL_SRC_ALPHA,
            GLES20.GL_ONE_MINUS_SRC_ALPHA
        )

        // =========================
        // TODO
        // =========================

        // shaders
        // textures
        // sphere mesh
        // VBO / VAO / IBO
    }

    fun onViewportChanged(
        width: Int,
        height: Int
    ) {

        GLES20.glViewport(
            0,
            0,
            width,
            height
        )

        // TODO:
        // projection matrix
        // aspect ratio
    }

    fun render(
        commands: List<RenderCommand>
    ) {

        commands.forEach { command ->

            when (command) {

                is RenderCommand.SetMatrix -> {
                    setMatrix(command.matrix)
                }

                is RenderCommand.SetTexture -> {
                    bindTexture(command.textureId)
                }

                is RenderCommand.DrawMesh -> {
                    drawMesh(command.objectId)
                }
            }
        }
    }

    private fun setMatrix(
        matrix: TransformMatrix
    ) {

        currentMatrix = matrix

        // TODO:
        // glUniformMatrix4fv()

    }

    private fun bindTexture(
        textureId: String
    ) {

        // TODO:
        // texture cache
        // glBindTexture()

    }

    private fun drawMesh(
        id: Long
    ) {

        // TODO:
        // mesh repository lookup
        // glBindBuffer()
        // glVertexAttribPointer()
        // glDrawElements()

    }
}