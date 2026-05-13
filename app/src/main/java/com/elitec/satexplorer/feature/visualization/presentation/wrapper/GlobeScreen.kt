package com.elitec.satexplorer.feature.visualization.presentation.wrapper

import android.opengl.GLSurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.elitec.satexplorer.feature.tracking.domain.entity.Vector3D
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObject
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObjectType
import com.elitec.satexplorer.feature.visualization.presentation.renderer.GlSurfaceRenderer
import com.elitec.satexplorer.feature.visualization.presentation.viewmodel.VisualizationViewModel
import org.koin.compose.koinInject

@Composable
fun GlobeScreen(
    viewModel: VisualizationViewModel = koinInject(),
    renderer: GlSurfaceRenderer = koinInject()
) {

    val context = LocalContext.current

    val glView = remember {
        GLSurfaceView(context).apply {
            setEGLContextClientVersion(2)
            setRenderer(renderer)
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }
    }

    AndroidView(
        factory = { glView }
    )

    LaunchedEffect(Unit) {
        viewModel.setObjects(
            listOf(
                RenderObject(
                    id = 1,
                    type = RenderObjectType.GLOBE,
                    position = Vector3D(0.0, 0.0, 0.0),
                    rotation = Vector3D(0.0, 0.0, 0.0),
                    scale = Vector3D(1.0, 1.0, 1.0),
                    isVisible = true,
                    layer = 0
                ),
                RenderObject(
                    id = 2,
                    type = RenderObjectType.SATELLITE,
                    position = Vector3D(1.0, 0.2, 0.0),
                    rotation = Vector3D(0.0, 0.0, 0.0),
                    scale = Vector3D(0.03, 0.03, 0.03),
                    isVisible = true,
                    layer = 1
                )
            )
        )
    }
}