package com.elitec.satexplorer.feature.tracking.presentation.screens

import android.Manifest
import android.content.Context
import android.os.Build
import android.view.Surface
import android.view.WindowManager
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.elitec.satexplorer.feature.tracking.presentation.viewmodel.ArTrackerViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ArTrackerScreen(
    modifier: Modifier = Modifier,
    viewModel: ArTrackerViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    
    // Obtener y escuchar la rotación física de la pantalla (Portrait/Landscape) de forma retrocompatible
    val display = remember(context, configuration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                context.display
            } catch (e: Exception) {
                (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            }
        } else {
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        }
    }
    
    val rotation = display?.rotation ?: Surface.ROTATION_0
    
    // Pasar la rotación de la pantalla al ViewModel al cambiar
    LaunchedEffect(rotation) {
        viewModel.updateScreenRotation(rotation)
    }
    
    // Configuración de permisos usando Accompanist
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    if (permissionState.allPermissionsGranted) {
        ArTrackerContent(viewModel = viewModel, modifier = modifier)
    } else {
        // Pantalla de solicitud de permisos
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF05070D)), // Deep Space Black
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .border(2.dp, Color(0xFF00D9FF), RoundedCornerShape(40.dp)), // Electric Cyan
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "AR",
                        color = Color(0xFF00D9FF),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "SEGUIMIENTO EN EL CIELO",
                    color = Color(0xFFF5F7FA),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Para poder proyectar los satélites sobre tu cielo local en tiempo real, SatExplorer requiere acceso a tu cámara y a tu ubicación GPS exacta.",
                    color = Color(0xFFA6B1C2),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Button(
                    onClick = { permissionState.launchMultiplePermissionRequest() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00D9FF),
                        contentColor = Color(0xFF05070D)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "CONCEDER PERMISOS",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ArTrackerContent(
    viewModel: ArTrackerViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val deviceAzimuth by viewModel.deviceAzimuth.collectAsState()
    val devicePitch by viewModel.devicePitch.collectAsState()
    val satellite by viewModel.targetSatellite.collectAsState()
    val orbitPath by viewModel.orbitPath.collectAsState()
    val cameraRotationMatrix by viewModel.cameraRotationMatrix.collectAsState()

    DisposableEffect(Unit) {
        viewModel.registerSensors()
        onDispose { viewModel.unregisterSensors() }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 1. FONDO DE LA CÁMARA (CameraX Preview)
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = androidx.camera.core.Preview.Builder().build().also {
                            it.setSurfaceProvider(surfaceProvider)
                        }
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA,
                                preview
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 2. REALIDAD AUMENTADA OVERLAY (Dibujado con sensor de fusión)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerX = width / 2
            val centerY = height / 2

            // El horizonte físico corresponde a la elevación de la cámara (devicePitch)
            val cameraElevation = devicePitch

            // FILTRO DE HORIZONTE:
            // Si la inclinación del móvil cae por debajo de 0 grados (apuntando al suelo)
            // o el satélite está teóricamente bajo el horizonte local (Elevación satélite < 0), no pintamos nada.
            if (cameraElevation >= -5f && satellite.elevation >= 0f) {
                
                // Campo de visión (FOV) dinámico de la cámara según la orientación de la pantalla (Portrait vs Landscape)
                val isLandscape = width > height
                val fov = if (isLandscape) 75f else 55f

                // Calcular longitud focal para proyección de perspectiva
                val fovRadians = Math.toRadians(fov.toDouble())
                val focalLength = (width / 2) / Math.tan(fovRadians / 2).toFloat()

                // Proyectar el satélite usando perspectiva 3D
                val satPoint = projectSkyPosition(
                    azimuth = satellite.azimuth,
                    elevation = satellite.elevation,
                    rotationMatrix = cameraRotationMatrix,
                    centerX = centerX,
                    centerY = centerY,
                    focalLength = focalLength
                )

                // 1. Dibujar la órbita completa uniendo los puntos proyectados
                if (orbitPath.isNotEmpty()) {
                    var lastPoint: Offset? = null
                    for (p in orbitPath) {
                        val currentPoint = projectSkyPosition(
                            azimuth = p.azimuth,
                            elevation = p.elevation,
                            rotationMatrix = cameraRotationMatrix,
                            centerX = centerX,
                            centerY = centerY,
                            focalLength = focalLength
                        )
                        
                        if (lastPoint != null && currentPoint != null) {
                            // Evitar dibujar líneas cruzadas glitch que atraviesan la pantalla
                            val distance = (currentPoint - lastPoint).getDistance()
                            if (distance < width * 0.4f) {
                                drawLine(
                                    color = Color(0xFF00D9FF).copy(alpha = 0.4f),
                                    start = lastPoint,
                                    end = currentPoint,
                                    strokeWidth = 6f
                                )
                            }
                        }
                        lastPoint = currentPoint
                    }
                }

                // 2. Dibujar el satélite principal y su HUD si está dentro del encuadre (y en frente de la cámara)
                if (satPoint != null && satPoint.x >= 0f && satPoint.x <= width && satPoint.y >= 0f && satPoint.y <= height) {
                    val satX = satPoint.x
                    val satY = satPoint.y

                    // 3. Dibujar el vector de movimiento direccional (flecha verde en el cielo)
                    val nextSatPoint = projectSkyPosition(
                        azimuth = satellite.nextAzimuth,
                        elevation = satellite.nextElevation,
                        rotationMatrix = cameraRotationMatrix,
                        centerX = centerX,
                        centerY = centerY,
                        focalLength = focalLength
                    )

                    if (nextSatPoint != null) {
                        val dx = nextSatPoint.x - satPoint.x
                        val dy = nextSatPoint.y - satPoint.y
                        val length = Math.hypot(dx.toDouble(), dy.toDouble()).toFloat()

                        if (length > 0f) {
                            val dirX = dx / length
                            val dirY = dy / length

                            // Iniciar vector desde el borde exterior del satélite (24dp de radio)
                            val startPadding = 24f
                            val vectorLength = 80f

                            val startPoint = Offset(satX + dirX * startPadding, satY + dirY * startPadding)
                            val endPoint = Offset(satX + dirX * (startPadding + vectorLength), satY + dirY * (startPadding + vectorLength))

                            // Dibujar línea del vector de movimiento (Verde brillante)
                            drawLine(
                                color = Color(0xFF31E981),
                                start = startPoint,
                                end = endPoint,
                                strokeWidth = 5f
                            )

                            // Dibujar la punta de flecha en el extremo del vector
                            val arrowSize = 16f
                            val arrowAngle = Math.atan2(dirY.toDouble(), dirX.toDouble()).toFloat()

                            val arrowPath = Path().apply {
                                moveTo(endPoint.x, endPoint.y)
                                lineTo(
                                    (endPoint.x - arrowSize * Math.cos(arrowAngle - Math.PI / 6)).toFloat(),
                                    (endPoint.y - arrowSize * Math.sin(arrowAngle - Math.PI / 6)).toFloat()
                                )
                                lineTo(
                                    (endPoint.x - arrowSize * Math.cos(arrowAngle + Math.PI / 6)).toFloat(),
                                    (endPoint.y - arrowSize * Math.sin(arrowAngle + Math.PI / 6)).toFloat()
                                )
                                close()
                            }

                            drawPath(
                                path = arrowPath,
                                color = Color(0xFF31E981)
                            )
                        }
                    }

                    // 4. Círculo de rastreo exterior del HUD (Electric Cyan traslúcido)
                    drawCircle(
                        color = Color(0xFF00D9FF).copy(alpha = 0.3f),
                        radius = 48f,
                        center = Offset(satX, satY),
                        style = Stroke(width = 3f)
                    )

                    // 5. Marcador del satélite principal (Electric Cyan sólido)
                    drawCircle(
                        color = Color(0xFF00D9FF),
                        radius = 16f,
                        center = Offset(satX, satY)
                    )
                }
            }
        }

        // HUD de Telemetría inferior
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xDD0A1220) // Midnight Navy traslúcido
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = satellite.name,
                        color = Color(0xFFF5F7FA),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "LIVE",
                        color = Color(0xFF31E981),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0xFF31E981).copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = "Próximo paso visible: ${satellite.nextPassTime}",
                    color = Color(0xFFFFC857),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                HorizontalDivider(color = Color(0xFF131C2E).copy(alpha = 0.5f), thickness = 1.dp)

                Text(
                    text = "Instrucciones: Gira al Sur (180°) y eleva el móvil 45°. El satélite y su órbita con vector direccional solo se dibujan al apuntar sobre el horizonte.",
                    color = Color(0xFFA6B1C2),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

private fun normalizeAngleDiff(diff: Float): Float {
    var d = diff
    while (d < -180f) d += 360f
    while (d > 180f) d -= 360f
    return d
}

private fun projectSkyPosition(
    azimuth: Float,
    elevation: Float,
    rotationMatrix: FloatArray,
    centerX: Float,
    centerY: Float,
    focalLength: Float
): Offset? {
    val azRad = Math.toRadians(azimuth.toDouble())
    val elRad = Math.toRadians(elevation.toDouble())
    val cosEl = Math.cos(elRad)
    val xW = (cosEl * Math.sin(azRad)).toFloat()
    val yW = (cosEl * Math.cos(azRad)).toFloat()
    val zW = Math.sin(elRad).toFloat()

    // V_camera = R^T * V_world
    // rotationMatrix es de 3x3 en orden de filas:
    // [r0 r1 r2]
    // [r3 r4 r5]
    // [r6 r7 r8]
    // La transpuesta R^T multiplica así:
    val xC = rotationMatrix[0] * xW + rotationMatrix[3] * yW + rotationMatrix[6] * zW
    val yC = rotationMatrix[1] * xW + rotationMatrix[4] * yW + rotationMatrix[7] * zW
    val zC = rotationMatrix[2] * xW + rotationMatrix[5] * yW + rotationMatrix[8] * zW

    // Si zC >= 0, el punto está detrás del plano de la cámara
    if (zC >= 0f) return null

    val screenX = centerX + (xC / -zC) * focalLength
    val screenY = centerY - (yC / -zC) * focalLength
    return Offset(screenX, screenY)
}
