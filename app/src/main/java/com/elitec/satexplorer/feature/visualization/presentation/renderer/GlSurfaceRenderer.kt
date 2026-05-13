package com.elitec.satexplorer.feature.visualization.presentation.renderer

import android.opengl.GLSurfaceView
import com.elitec.satexplorer.feature.visualization.data.renderEngine.OpenGlRendererEngine
import com.elitec.satexplorer.feature.visualization.domain.caseuse.BuildSceneGraphUseCase
import com.elitec.satexplorer.feature.visualization.domain.caseuse.GenerateRenderCommandsUseCase
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObject
import com.elitec.satexplorer.feature.visualization.presentation.util.RenderStateHolder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GlSurfaceRenderer(
    private val engine: OpenGlRendererEngine,
    private val generateCommands: GenerateRenderCommandsUseCase,
    private val stateHolder: RenderStateHolder
) : GLSurfaceView.Renderer {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // init shaders, depth, etc
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // viewport setup
    }

    override fun onDrawFrame(gl: GL10?) {

        val sceneGraph = stateHolder.sceneGraph

        val commands = generateCommands(sceneGraph)

        engine.render(commands)
    }
}