package com.elitec.satexplorer.feature.visualization.presentation.wrapper

import android.opengl.GLSurfaceView
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elitec.satexplorer.feature.tracking.domain.entity.Vector3D
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObject
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObjectType
import com.elitec.satexplorer.feature.visualization.presentation.renderer.GlSurfaceRenderer
import com.elitec.satexplorer.feature.visualization.presentation.viewmodel.VisualizationViewModel
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Composable
fun GlobeScreen(
    modifier: Modifier = Modifier,
    viewModel: VisualizationViewModel = koinInject(),
    renderer: GlSurfaceRenderer = koinInject(),
) {

    val context = LocalContext.current
    val controls by viewModel.controlsState.collectAsStateWithLifecycle()

    val glView = remember(renderer, context) {
        GLSurfaceView(context).apply {
            setEGLContextClientVersion(2)
            setRenderer(renderer)
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxSize()
    ) {
        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { glView }
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Rotación")
            RepeatButton(onRepeat = viewModel::rotateUp, label = "↑")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RepeatButton(onRepeat = viewModel::rotateLeft, label = "←")
                RepeatButton(onRepeat = viewModel::rotateRight, label = "→")
            }
            RepeatButton(onRepeat = viewModel::rotateDown, label = "↓")
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Zoom")
            Slider(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(0.35f),
                value = controls.cameraDistance,
                onValueChange = viewModel::updateZoom,
                valueRange = 1.25f..10f
            )
            Text(if (controls.cameraDistance > 5.6f) "Estirar" else "Agrandar")
        }
    }

    LaunchedEffect(controls.yaw, controls.pitch) {
        renderer.setOrbit(controls.yaw, controls.pitch)
    }

    LaunchedEffect(controls.cameraDistance) {
        renderer.setDistance(controls.cameraDistance)
    }

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

@Composable
private fun RepeatButton(
    onRepeat: () -> Unit,
    label: String,
    intervalMs: Long = 33L
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        if (isPressed) {
            onRepeat()
            while (isPressed) {
                delay(intervalMs)
                onRepeat()
            }
        }
    }

    Button(
        onClick = onRepeat,
        interactionSource = interactionSource
    ) {
        Text(label)
    }
}