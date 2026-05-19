package com.elitec.satexplorer.feature.visualization.presentation.wrapper

import android.opengl.GLSurfaceView
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material3.Button
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elitec.satexplorer.feature.tracking.domain.entity.Vector3D
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObject
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObjectType
import com.elitec.satexplorer.feature.visualization.presentation.components.RenderControl
import com.elitec.satexplorer.feature.visualization.presentation.renderer.GlSurfaceRenderer
import com.elitec.satexplorer.feature.visualization.presentation.viewmodel.VisualizationViewModel
import com.elitec.satexplorer.infrastructure.presentation.theme.signalGreen
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

    var actionIcon by remember { mutableStateOf<ImageVector?>(null) }

    LaunchedEffect(actionIcon) {
        delay(1500)
        actionIcon = null
    }

    val glView = remember(renderer, context) {
        GLSurfaceView(context).apply {
            setEGLContextClientVersion(2)
            setRenderer(renderer)
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
    ) {
        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { glView }
        )
        Row (
            Modifier
                .fillMaxWidth()
                .padding(
                    10.dp
                )
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
             verticalAlignment = Alignment.Top
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Rotation"
                )
                RenderControl({
                    viewModel.rotateUp()
                    actionIcon = Icons.Default.KeyboardArrowUp
                }, {
                    viewModel.rotateDown()
                    actionIcon = Icons.Default.KeyboardArrowDown
                }, {
                    viewModel.rotateLeft()
                    actionIcon = Icons.AutoMirrored.Filled.KeyboardArrowLeft
                }, {
                    viewModel.rotateRight()
                    actionIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight
                }, {
                    actionIcon = Icons.Default.GpsFixed
                }
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Zoom"
                )
                Slider(
                    modifier = Modifier
                        .size(100.dp)
                        .fillMaxWidth(0.35f)
                        .rotate(90f),
                    value = controls.cameraDistance,
                    onValueChange = {
                        viewModel.updateZoom(it)

                    },
                    valueRange = 1.25f..10f
                )
            }

        }

        TelemetrySection(
            modifier = Modifier.width(130.dp)
                .align(Alignment.TopEnd)
                .padding(10.dp)
        )
        AnimatedVisibility(
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp),
            visible = actionIcon != null
        ) {
            actionIcon?.let { icon ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = signalGreen.copy(0.2f),
                    border = BorderStroke(1.dp,signalGreen)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "action icon"
                    )
                }
            }
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
private fun TelemetrySection(
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier,
        border = BorderStroke(
            1.dp,
            Brush.linearGradient(
                listOf(
                    MaterialTheme.colorScheme.primary.copy(0.8f),
                    Color.Transparent
                )
            )
        ),
        color = MaterialTheme.colorScheme.primary.copy(0.1f)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Altitude"
                )
                Text(
                    fontWeight = FontWeight.Bold,
                    text = "342 Km",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            CustomHorizontalDivider(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.primary,
                        Color.Transparent
                    )
                ),
                height = 2.dp
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Velocity"
                )
                Text(
                    fontWeight = FontWeight.Bold,
                    text = "1002 Km/h",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            CustomHorizontalDivider(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.primary,
                        Color.Transparent
                    )
                ),
                height = 2.dp
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Inclination"
                )
                Text(
                    fontWeight = FontWeight.Bold,
                    text = "51.45°",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun CustomHorizontalDivider(
    brush: Brush,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
            .height(height)
            .background(
                brush
            )
    )
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