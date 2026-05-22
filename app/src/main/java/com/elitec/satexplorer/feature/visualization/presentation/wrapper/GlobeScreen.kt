package com.elitec.satexplorer.feature.visualization.presentation.wrapper

import android.opengl.GLSurfaceView
import android.opengl.Matrix
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import com.elitec.satexplorer.feature.tracking.domain.entity.Vector3D
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObject
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObjectType
import com.elitec.satexplorer.feature.visualization.presentation.components.RenderControl
import com.elitec.satexplorer.feature.visualization.presentation.renderer.GlSurfaceRenderer
import com.elitec.satexplorer.feature.visualization.presentation.viewmodel.VisualizationViewModel
import com.elitec.satexplorer.infrastructure.presentation.theme.signalGreen
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

@Composable
fun GlobeScreen(
    modifier: Modifier = Modifier,
    selectedSatellite: Satellite? = null,
    viewModel: VisualizationViewModel = koinInject(),
    renderer: GlSurfaceRenderer = koinInject(),
) {

    val context = LocalContext.current
    val controls by viewModel.controlsState.collectAsStateWithLifecycle()
    val renderObjects by viewModel.renderObjects.collectAsStateWithLifecycle()

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
                    viewModel.centerOnSatellite()
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
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)
        ) {
            selectedSatellite?.let { satellite ->
                SelectedSatelliteBadge(
                    satellite = satellite,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            TelemetrySection(
                modifier = Modifier.width(130.dp)
                    .padding(10.dp)
            )

        }

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

    LaunchedEffect(selectedSatellite?.noradId) {
        selectedSatellite?.let { satellite ->
            val isGeostationary = satellite.type.name == "GEO" || satellite.tle.meanMotion in 0.99..1.01
            val orbitRadius = when {
                satellite.tle.meanMotion >= 11.0 -> 1.22
                satellite.tle.meanMotion >= 2.0 -> 1.72
                else -> 2.28
            }
            val eccentricity = satellite.tle.eccentricity.coerceIn(0.0, 0.95)
            val minorAxis = orbitRadius * sqrt(1.0 - eccentricity * eccentricity)
            val phase = eccentricAnomalyFromMeanAnomaly(
                meanAnomalyDeg = satellite.tle.meanAnomaly,
                eccentricity = eccentricity
            )

            val orbitRotation = Vector3D(
                satellite.tle.inclination,
                0.0,
                satellite.tle.raan + satellite.tle.argumentOfPerigee
            )
            val pos = orbitPointWorld(
                phase = phase,
                orbitRadius = orbitRadius,
                minorAxis = minorAxis,
                rotation = orbitRotation
            )

            val (targetYaw, targetPitch) = satelliteCameraPose(pos)
            val yawAnim = Animatable(controls.yaw)
            val pitchAnim = Animatable(controls.pitch)
            val distAnim = Animatable(controls.cameraDistance)
            coroutineScope {
                launch { yawAnim.animateTo(targetYaw, animationSpec = tween(durationMillis = 900)) }
                launch { pitchAnim.animateTo(targetPitch, animationSpec = tween(durationMillis = 900)) }
                launch { distAnim.animateTo((orbitRadius + 0.9).toFloat().coerceIn(1.45f, 4.8f), animationSpec = tween(durationMillis = 900)) }
                while (yawAnim.isRunning || pitchAnim.isRunning || distAnim.isRunning) {
                    viewModel.setCameraPose(yawAnim.value, pitchAnim.value, distAnim.value)
                    delay(16)
                }
            }
            viewModel.setCameraPose(targetYaw, targetPitch, (orbitRadius + 0.9).toFloat().coerceIn(1.45f, 4.8f))

            viewModel.setObjects(
                buildList {
                    add(
                        RenderObject(
                            id = 1,
                            type = RenderObjectType.GLOBE,
                            position = Vector3D(0.0, 0.0, 0.0),
                            rotation = orbitRotation,
                            scale = Vector3D(1.0, 1.0, 1.0),
                            isVisible = true,
                            layer = 0
                        )
                    )
                    if (!isGeostationary) {
                        add(
                            RenderObject(
                                id = 4,
                                type = RenderObjectType.ORBIT_PATH,
                                position = Vector3D(0.0, 0.0, 0.0),
                                rotation = orbitRotation,
                                scale = Vector3D(orbitRadius, minorAxis, orbitRadius),
                                isVisible = true,
                                layer = 1
                            )
                        )
                    }
                    add(
                        RenderObject(
                            id = 2,
                            type = RenderObjectType.SATELLITE,
                            position = pos,
                            rotation = Vector3D(0.0, 0.0, 0.0),
                            scale = Vector3D(0.024, 0.024, 0.024),
                            isVisible = true,
                            layer = 2
                        )
                    )
                }
            )
        }
    }

    LaunchedEffect(renderObjects.isEmpty()) {
        if (renderObjects.isEmpty()) {
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
                    ),
                    RenderObject(
                        id = 3,
                        type = RenderObjectType.SATELLITE,
                        position = Vector3D(1.25, 0.3, 0.0),
                        rotation = Vector3D(0.0, 0.0, 0.0),
                        scale = Vector3D(0.018, 0.018, 0.018),
                        isVisible = true,
                        layer = 2
                    )
                )
            )
        }
    }
}

private fun eccentricAnomalyFromMeanAnomaly(meanAnomalyDeg: Double, eccentricity: Double): Double {
    val meanAnomaly = Math.toRadians(meanAnomalyDeg)
    if (eccentricity < 1e-6) return meanAnomaly

    var eccentricAnomaly = meanAnomaly
    repeat(8) {
        val f = eccentricAnomaly - eccentricity * sin(eccentricAnomaly) - meanAnomaly
        val fp = 1.0 - eccentricity * cos(eccentricAnomaly)
        eccentricAnomaly -= f / fp
    }
    return eccentricAnomaly
}


private fun trueAnomalyFromMeanAnomaly(meanAnomalyDeg: Double, eccentricity: Double): Double {
    val meanAnomaly = Math.toRadians(meanAnomalyDeg)
    if (eccentricity < 1e-6) return meanAnomaly

    var eccentricAnomaly = meanAnomaly
    repeat(8) {
        val f = eccentricAnomaly - eccentricity * sin(eccentricAnomaly) - meanAnomaly
        val fp = 1.0 - eccentricity * cos(eccentricAnomaly)
        eccentricAnomaly -= f / fp
    }

    val factor = sqrt((1.0 + eccentricity) / (1.0 - eccentricity))
    return 2.0 * atan2(factor * tan(eccentricAnomaly / 2.0), 1.0)
}

@Composable
private fun SelectedSatelliteBadge(
    satellite: Satellite,
    modifier: Modifier = Modifier
) {
    val orbitLabel = if (satellite.type.name == "GEO" || satellite.tle.meanMotion in 0.99..1.01) {
        "GEOSTATIONARY ORBIT"
    } else {
        "${satellite.type.name} ORBIT"
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.82f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)),
        modifier = modifier.widthIn(max = 220.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                text = orbitLabel
            )
            Text(
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                text = satellite.name
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = "NORAD ${satellite.noradId}"
            )
            if (satellite.type.name == "GEO" || satellite.tle.meanMotion in 0.99..1.01) {
                Text(
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                    text = "Sin traza: satélite geoestacionario"
                )
            }
        }
    }
}

private fun orbitPointWorld(phase: Double, orbitRadius: Double, minorAxis: Double, rotation: Vector3D): Vector3D {
    val matrix = FloatArray(16)
    Matrix.setIdentityM(matrix, 0)
    Matrix.rotateM(matrix, 0, rotation.x.toFloat(), 1f, 0f, 0f)
    Matrix.rotateM(matrix, 0, rotation.y.toFloat(), 0f, 1f, 0f)
    Matrix.rotateM(matrix, 0, rotation.z.toFloat(), 0f, 0f, 1f)
    Matrix.scaleM(matrix, 0, orbitRadius.toFloat(), minorAxis.toFloat(), orbitRadius.toFloat())

    val local = floatArrayOf(cos(phase).toFloat(), sin(phase).toFloat(), 0f, 1f)
    val world = FloatArray(4)
    Matrix.multiplyMV(world, 0, matrix, 0, local, 0)
    return Vector3D(world[0].toDouble(), world[1].toDouble(), world[2].toDouble())
}

private fun satelliteCameraPose(position: Vector3D): Pair<Float, Float> {
    val yaw = Math.toDegrees(atan2(position.x, position.z)).toFloat()
    val horizontal = sqrt(position.x * position.x + position.z * position.z)
    val pitch = Math.toDegrees(atan2(position.y, horizontal)).toFloat().coerceIn(-70f, 70f)
    return yaw to pitch
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
