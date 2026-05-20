package com.elitec.satexplorer.feature.tracking.presentation.viewmodel

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import com.elitec.satexplorer.feature.tracking.domain.entity.SkyPosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
class ArTrackerViewModel(
    private val sensorManager: SensorManager
) : ViewModel(), SensorEventListener {
    private val _deviceAzimuth = MutableStateFlow(0f)
    private val _devicePitch = MutableStateFlow(0f)

    // Satélite de prueba (Ubicación fija simulada en el cielo para desarrollo: Azimut 180° Sur, Elevación 45°)
    private val _targetSatellite = MutableStateFlow(
        SkyPosition(
            azimuth = 180f,
            elevation = 45f,
            name = "ISS (Estación Espacial)",
            nextPassTime = "14:32 (Visible por 6 min)"
        )
    )

    val deviceAzimuth: StateFlow<Float> = _deviceAzimuth.asStateFlow()
    val devicePitch: StateFlow<Float> = _devicePitch.asStateFlow()
    val targetSatellite: StateFlow<SkyPosition> = _targetSatellite.asStateFlow()
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)
    private var accelerometerReading = FloatArray(3)
    private var magnetometerReading = FloatArray(3)
    fun registerSensors() {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }
    fun unregisterSensors() {
        sensorManager.unregisterListener(this)
    }
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            accelerometerReading = event.values.clone()
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetometerReading = event.values.clone()
        }
        if (SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)) {
            SensorManager.getOrientation(rotationMatrix, orientationAngles)

            // Convertimos radianes a grados y normalizamos el azimut (0..360)
            val azimuthDeg = (Math.toDegrees(orientationAngles[0].toDouble()).toFloat() + 360f) % 360f
            val pitchDeg = Math.toDegrees(orientationAngles[1].toDouble()).toFloat() // Inclinación vertical
            _deviceAzimuth.value = azimuthDeg
            _devicePitch.value = pitchDeg
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}