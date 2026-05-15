package com.elitec.satexplorer.feature.visualization.presentation.wrapper

import android.opengl.GLSurfaceView
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier,
    viewModel: VisualizationViewModel = koinInject(),
    renderer: GlSurfaceRenderer = koinInject(),
) {

    val context = LocalContext.current

    val glView = remember(renderer, context) {
        GLSurfaceView(context).apply {
            setEGLContextClientVersion(2)
            setRenderer(renderer)
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

            val scaleDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    renderer.setZoom(detector.scaleFactor)
                    return true
                }
            })

            var lastX = 0f
            var lastY = 0f
            var downX = 0f
            var downY = 0f
            setOnTouchListener { view, event ->
                scaleDetector.onTouchEvent(event)
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.x
                        lastY = event.y
                        downX = event.x
                        downY = event.y
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = event.x - lastX
                        val dy = event.y - lastY
                        lastX = event.x
                        lastY = event.y
                        if (!scaleDetector.isInProgress) {
                            renderer.orbit(dx, dy)
                        }
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        val clickSlop = 12f
                        val moved = kotlin.math.abs(event.x - downX) > clickSlop ||
                                kotlin.math.abs(event.y - downY) > clickSlop
                        if (!moved && !scaleDetector.isInProgress) {
                            view.performClick()
                        }
                        true
                    }
                    else -> false
                }
            }
        }
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
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
                    position = Vector3D(1.25, 0.3, 0.0),
                    rotation = Vector3D(0.0, 0.0, 0.0),
                    scale = Vector3D(0.03, 0.03, 0.03),
                    isVisible = true,
                    layer = 1
                )
            )
        )
    }
}