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

    fun setZoom(scaleFactor: Float) = engine.setCameraZoom(scaleFactor)

    fun setOrbit(yaw: Float, pitch: Float) = engine.setCameraOrbit(yaw, pitch)

    fun setDistance(distance: Float) = engine.setCameraDistance(distance)
    fun orbit(deltaX: Float, deltaY: Float) = engine.orbitCamera(deltaX, deltaY)

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.04f, 0.05f, 0.1f, 1f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        engine.init()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        engine.onViewportChanged(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        val sceneGraph = stateHolder.sceneGraph
        val commands = generateCommands(sceneGraph)
        engine.render(commands)
    }
}