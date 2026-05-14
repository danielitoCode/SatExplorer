package com.elitec.satexplorer.feature.visualization.presentation.renderer

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.elitec.satexplorer.feature.visualization.data.renderEngine.OpenGlRendererEngine
import com.elitec.satexplorer.feature.visualization.domain.caseuse.GenerateRenderCommandsUseCase
import com.elitec.satexplorer.feature.visualization.presentation.util.RenderStateHolder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GlSurfaceRenderer(
    private val engine: OpenGlRendererEngine,
    private val generateCommands: GenerateRenderCommandsUseCase,
    private val stateHolder: RenderStateHolder
) : GLSurfaceView.Renderer {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 🔥 COLOR DE FONDO (si esto falla verás negro eterno)
        GLES20.glClearColor(0.05f, 0.05f, 0.1f, 1f)

        // 🔥 PROFUNDIDAD (IMPORTANTE PARA 3D)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        // 🔥 BLENDING (opcional pero útil luego para atmósfera / UI)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(
            GLES20.GL_SRC_ALPHA,
            GLES20.GL_ONE_MINUS_SRC_ALPHA
        )

        // 👉 inicialización del engine (shaders, buffers, etc)
        engine.init()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // viewport setup
        // 🔥 VIEWPORT OBLIGATORIO
        GLES20.glViewport(0, 0, width, height)

        // 🔥 opcional: aquí iría tu projection matrix
        engine.onViewportChanged(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {

        // 🔥 LIMPIAR FRAME
        GLES20.glClear(
            GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT
        )

        // 📦 snapshot de escena
        val sceneGraph = stateHolder.sceneGraph

        // 🔄 scene → commands
        val commands = generateCommands(sceneGraph)

        // 🚀 render GPU
        engine.render(commands)
    }
}