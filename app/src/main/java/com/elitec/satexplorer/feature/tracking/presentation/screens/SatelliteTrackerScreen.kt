package com.elitec.satexplorer.feature.tracking.presentation.screens

import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.elitec.satexplorer.feature.tracking.presentation.viewmodel.ArTrackerViewModel
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
@Composable
fun ArTrackerScreen(
    viewModel: ArTrackerViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val deviceAzimuth by viewModel.deviceAzimuth.collectAsState()
    val devicePitch by viewModel.devicePitch.collectAsState()
    val satellite by viewModel.targetSatellite.collectAsState()
    // Registrar sensores en el ciclo de vida de Compose
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
        // 2. REALIDAD AUMENTADA OVERLAY (Dibujado de la trayectoria y satélite)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerX = width / 2
            val centerY = height / 2
            // El campo de visión de la cámara del teléfono (FOV) es de aprox. 60° horizontal / vertical
            val fov = 60f
            val pixelsPerDegree = width / fov
            // Calculamos la diferencia angular entre la cámara del teléfono y el satélite
            val diffAzimuth = (satellite.azimuth - deviceAzimuth)
            val diffPitch = (satellite.elevation - (devicePitch + 90f)) // Ajuste del ángulo del móvil
            // Si el satélite está dentro del campo de visión de la pantalla, lo dibujamos
            if (Math.abs(diffAzimuth) < (fov / 2) && Math.abs(diffPitch) < (fov / 2)) {
                val satX = centerX + (diffAzimuth * pixelsPerDegree)
                val satY = centerY - (diffPitch * pixelsPerDegree)
                // Dibujar el punto del satélite (Círculo Cyan brillante)
                drawCircle(
                    color = Color(0xFF00D9FF),
                    radius = 24f,
                    center = Offset(satX, satY)
                )
                // Dibujar órbita simulada (Línea punteada o continua que pasa por el satélite)
                drawLine(
                    color = Color(0xFF00D9FF).copy(alpha = 0.5f),
                    start = Offset(satX - 150f, satY + 50f),
                    end = Offset(satX + 150f, satY - 50f),
                    strokeWidth = 5f
                )
            }
        }
        // 3. TARJETA INFORMATIVA CON LA HORA DEL PRÓXIMO PASO
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xAA0A1220)) // Vidrio traslúcido
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = satellite.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    text = "Próximo paso visible: ${satellite.nextPassTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFFFC857) // Amarillo señalización
                )
                Text(
                    text = "Apunta tu teléfono hacia el Sur (180°) y elévate a 45° para ubicarlo.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
        }
    }
}