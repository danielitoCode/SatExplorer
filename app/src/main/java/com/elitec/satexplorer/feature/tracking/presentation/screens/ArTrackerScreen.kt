package com.elitec.satexplorer.feature.tracking.presentation.screens

import android.Manifest
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
import androidx.compose.ui.graphics.drawscope.Stroke
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
    
    // Configuración de permisos usando Accompanist Permissions
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    if (permissionState.allPermissionsGranted) {
        // Ejecutar la vista de AR Tracker si todos los permisos están concedidos
        ArTrackerContent(viewModel = viewModel, modifier = modifier)
    } else {
        // Mostrar pantalla de solicitud de permisos con diseño premium futurista
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
                // Indicador de estado orbital
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
                    color = Color(0xFFF5F7FA), // Primary Text
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Para poder proyectar los satélites sobre tu cielo local en tiempo real, SatExplorer requiere acceso a tu cámara y a tu ubicación GPS exacta.",
                    color = Color(0xFFA6B1C2), // Secondary Text
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Button(
                    onClick = { permissionState.launchMultiplePermissionRequest() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00D9FF), // Electric Cyan
                        contentColor = Color(0xFF05070D)  // Deep Space Black
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

    // Registrar y desregistrar sensores en el ciclo de vida de Compose
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

        // 2. REALIDAD AUMENTADA OVERLAY (Dibujado en tiempo real)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerX = width / 2
            val centerY = height / 2

            // Campo de visión de la cámara móvil (aprox 60 grados)
            val fov = 60f
            val pixelsPerDegree = width / fov

            // Diferencia angular
            val diffAzimuth = (satellite.azimuth - deviceAzimuth)
            val diffPitch = (satellite.elevation - (devicePitch + 90f))

            if (Math.abs(diffAzimuth) < (fov / 2) && Math.abs(diffPitch) < (fov / 2)) {
                val satX = centerX + (diffAzimuth * pixelsPerDegree)
                val satY = centerY - (diffPitch * pixelsPerDegree)

                // Trayectoria simulada / órbita en el cielo
                drawLine(
                    color = Color(0xFF00D9FF).copy(alpha = 0.4f),
                    start = Offset(satX - 250f, satY + 100f),
                    end = Offset(satX + 250f, satY - 100f),
                    strokeWidth = 6f
                )

                // Círculo de rastreo exterior animado
                drawCircle(
                    color = Color(0xFF00D9FF).copy(alpha = 0.3f),
                    radius = 48f,
                    center = Offset(satX, satY),
                    style = Stroke(width = 3f)
                )

                // Marcador del satélite principal (Electric Cyan)
                drawCircle(
                    color = Color(0xFF00D9FF),
                    radius = 16f,
                    center = Offset(satX, satY)
                )
            }
        }

        // HUD de Telemetría e información del próximo paso
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
                        color = Color(0xFFF5F7FA), // Primary Text
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "LIVE",
                        color = Color(0xFF31E981), // Signal Green
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0xFF31E981).copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = "Próximo paso visible: ${satellite.nextPassTime}",
                    color = Color(0xFFFFC857), // Signal Amber
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Divider(color = Color(0xFF131C2E).copy(alpha = 0.5f), thickness = 1.dp)

                Text(
                    text = "Instrucciones: Gira a la dirección Sur (180°) y eleva el móvil unos 45° para que aparezca el satélite de prueba en pantalla.",
                    color = Color(0xFFA6B1C2), // Secondary Text
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
