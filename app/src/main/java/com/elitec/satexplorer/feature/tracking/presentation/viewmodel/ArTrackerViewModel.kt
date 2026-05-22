package com.elitec.satexplorer.feature.tracking.presentation.viewmodel

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Surface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elitec.satexplorer.feature.tracking.domain.entity.SkyPosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class ArTrackerViewModel(
    private val sensorManager: SensorManager
) : ViewModel(), SensorEventListener {

    private val _deviceAzimuth = MutableStateFlow(0f)
    private val _devicePitch = MutableStateFlow(0f)
    
    private val _targetSatellite = MutableStateFlow(
        SkyPosition(
            azimuth = 180f,
            elevation = 45f,
            name = "ISS (Estación Espacial)",
            nextPassTime = "14:32 (Visible por 6 min)",
            nextAzimuth = 181f,
            nextElevation = 45.2f
        )
    )

    private val _orbitPath = MutableStateFlow<List<SkyPosition>>(emptyList())
    private val _cameraRotationMatrix = MutableStateFlow(FloatArray(9).apply { this[0] = 1f; this[4] = 1f; this[8] = 1f })

    val deviceAzimuth: StateFlow<Float> = _deviceAzimuth.asStateFlow()
    val devicePitch: StateFlow<Float> = _devicePitch.asStateFlow()
    val targetSatellite: StateFlow<SkyPosition> = _targetSatellite.asStateFlow()
    val orbitPath: StateFlow<List<SkyPosition>> = _orbitPath.asStateFlow()
    val cameraRotationMatrix: StateFlow<FloatArray> = _cameraRotationMatrix.asStateFlow()

    init {
        generateOrbitPath()
        startSatelliteAnimation()
    }

    private fun generateOrbitPath() {
        val steps = 120
        _orbitPath.value = (0..steps).map { i ->
            val fraction = i.toFloat() / steps
            val (az, el) = orbitPositionAt(fraction)
            SkyPosition(
                azimuth = az,
                elevation = el,
                name = "ISS (Estación Espacial)",
                nextPassTime = "14:32 (Visible por 6 min)"
            )
        }
    }

    private fun orbitPositionAt(fraction: Float): Pair<Float, Float> {
        val normalized = fraction.coerceIn(0f, 1f)
        val startAz = 100f
        val endAz = 260f
        val maxEl = 55f
        val az = startAz + normalized * (endAz - startAz)
        val el = (maxEl * Math.sin(normalized * Math.PI)).toFloat()
        return az to el
    }

    private fun startSatelliteAnimation() {
        viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            val orbitDurationMs = 30000L // 30 segundos para una órbita
            while (isActive) {
                val elapsed = (System.currentTimeMillis() - startTime) % orbitDurationMs
                val fraction = elapsed.toFloat() / orbitDurationMs

                val (az, el) = orbitPositionAt(fraction)

                val nextFraction = ((elapsed + 150) % orbitDurationMs) / orbitDurationMs.toFloat()
                val (nextAz, nextEl) = orbitPositionAt(nextFraction)
                
                _targetSatellite.value = SkyPosition(
                    azimuth = az,
                    elevation = el,
                    name = "ISS (Estación Espacial)",
                    nextPassTime = "14:32 (Visible por 6 min)",
                    nextAzimuth = nextAz,
                    nextElevation = nextEl
                )
                
                delay(30)
            }
        }
    }

    private val rotationMatrix = FloatArray(9)
    private val remappedRotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)
    
    // Variables para fallback en caso de no contar con Sensor.TYPE_ROTATION_VECTOR
    private var accelerometerReading = FloatArray(3)
    private var magnetometerReading = FloatArray(3)
    private var hasRotationVectorSensor = false

    // Control de la rotación actual de la pantalla
    private var currentScreenRotation = Surface.ROTATION_0

    fun updateScreenRotation(rotation: Int) {
        currentScreenRotation = rotation
    }

    fun registerSensors() {
        val rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        if (rotationVectorSensor != null) {
            hasRotationVectorSensor = true
            sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            // Fallback si no tiene sensor de vector de rotación
            hasRotationVectorSensor = false
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
            sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
        }
    }

    fun unregisterSensors() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            processOrientation()
        } else {
            // Procesamiento de Fallback
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                accelerometerReading = event.values.clone()
            } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                magnetometerReading = event.values.clone()
            }
            
            if (SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)) {
                processOrientation()
            }
        }
    }

    private val tempCameraRotationMatrix = FloatArray(9)

    private fun processOrientation() {
        // Remapeo del sistema de coordenadas basado en la orientación física de la pantalla (Portrait/Landscape)
        var worldAxisX = SensorManager.AXIS_X
        var worldAxisY = SensorManager.AXIS_Y

        when (currentScreenRotation) {
            Surface.ROTATION_0 -> {
                worldAxisX = SensorManager.AXIS_X
                worldAxisY = SensorManager.AXIS_Y
            }
            Surface.ROTATION_90 -> {
                worldAxisX = SensorManager.AXIS_Y
                worldAxisY = SensorManager.AXIS_MINUS_X
            }
            Surface.ROTATION_180 -> {
                worldAxisX = SensorManager.AXIS_MINUS_X
                worldAxisY = SensorManager.AXIS_MINUS_Y
            }
            Surface.ROTATION_270 -> {
                worldAxisX = SensorManager.AXIS_MINUS_Y
                worldAxisY = SensorManager.AXIS_X
            }
        }

        // 1. Remapeo de rotación de pantalla
        SensorManager.remapCoordinateSystem(
            rotationMatrix,
            worldAxisX,
            worldAxisY,
            remappedRotationMatrix
        )

        // 2. Remapeo de perspectiva de la cámara (para alinear con el eje óptico Z cuando la cámara apunta al cielo)
        SensorManager.remapCoordinateSystem(
            remappedRotationMatrix,
            SensorManager.AXIS_X,
            SensorManager.AXIS_Z,
            tempCameraRotationMatrix
        )

        SensorManager.getOrientation(tempCameraRotationMatrix, orientationAngles)

        // Convertimos radianes a grados y normalizamos el azimut a 0..360
        val rawAzimuth = (Math.toDegrees(orientationAngles[0].toDouble()).toFloat() + 360f) % 360f
        // Negamos el pitch para que apuntar al cielo sea positivo (elevación) y al suelo sea negativo
        val rawPitch = -Math.toDegrees(orientationAngles[1].toDouble()).toFloat()

        // Coeficiente de suavizado del filtro de paso bajo (0.12 = muy estable, sin ruido de microvibraciones)
        val alpha = 0.12f

        _deviceAzimuth.value = smoothAngle(_deviceAzimuth.value, rawAzimuth, alpha)
        _devicePitch.value = _devicePitch.value + alpha * (rawPitch - _devicePitch.value)

        // Exponer la matriz de rotación de la cámara actualizada
        _cameraRotationMatrix.value = tempCameraRotationMatrix.clone()
    }

    /**
     * Aplica suavizado exponencial angular para evitar brincos en la brújula al cruzar la frontera de 360/0 grados.
     */
    private fun smoothAngle(current: Float, target: Float, alpha: Float): Float {
        var diff = target - current
        while (diff < -180f) diff += 360f
        while (diff > 180f) diff -= 360f
        return (current + alpha * diff + 360f) % 360f
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}