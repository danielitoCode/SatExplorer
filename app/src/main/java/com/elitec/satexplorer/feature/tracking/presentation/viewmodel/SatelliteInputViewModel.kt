package com.elitec.satexplorer.feature.tracking.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elitec.satexplorer.feature.tracking.domain.caseuse.ComputeSatellitePositionUseCase
import com.elitec.satexplorer.feature.tracking.domain.caseuse.LoadSatelliteFromApiUseCase
import com.elitec.satexplorer.feature.tracking.domain.caseuse.LoadSatelliteManualUseCase
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import com.elitec.satexplorer.feature.tracking.domain.entity.Vector3D
import com.elitec.satexplorer.feature.tracking.presentation.model.SatelliteUiState
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObject
import com.elitec.satexplorer.feature.visualization.domain.entity.RenderObjectType
import com.elitec.satexplorer.feature.visualization.presentation.viewmodel.VisualizationViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.sqrt

class SatelliteInputViewModel(
    private val loadApi: LoadSatelliteFromApiUseCase,
    private val loadManualUseCase: LoadSatelliteManualUseCase,
    private val computePosition: ComputeSatellitePositionUseCase,
    private val visualizationViewModel: VisualizationViewModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(SatelliteUiState())
    val uiState = _uiState.asStateFlow()
    private var animationJob: Job? = null

    fun loadFromApi(noradId: String) {
        viewModelScope.launch {
            _uiState.value = SatelliteUiState(isLoading = true)
            runCatching { loadApi(noradId.toInt()) }
                .onSuccess {
                    _uiState.value = SatelliteUiState(satellite = it)
                    animateSatellite()
                }
                .onFailure { _uiState.value = SatelliteUiState(error = it.message) }
        }
    }

    fun loadFromApi(noradId: Int) {
        loadFromApi(noradId.toString())
    }

    fun loadManual(name: String, line1: String, line2: String) {
        _uiState.value = runCatching { SatelliteUiState(satellite = loadManualUseCase(name, line1, line2)) }
            .getOrElse { SatelliteUiState(error = it.message) }
        animateSatellite()
    }

    private fun animateSatellite() {
        val sat = _uiState.value.satellite ?: return
        animationJob?.cancel()
        animationJob = viewModelScope.launch {
            var previous = computePosition(sat.tle, System.currentTimeMillis())
            while (isActive) {
                val now = System.currentTimeMillis()
                val pos = computePosition(sat.tle, now)
                val velocity = Vector3D(pos.x - previous.x, pos.y - previous.y, pos.z - previous.z)
                val speed = sqrt(velocity.x * velocity.x + velocity.y * velocity.y + velocity.z * velocity.z)
                val altitude = sqrt(pos.x * pos.x + pos.y * pos.y + pos.z * pos.z)
                val headingZDeg = Math.toDegrees(atan2(velocity.y, velocity.x))
                val arrowLength = (0.14 + speed * 30.0).coerceIn(0.14, 0.28)
                val satScale = (0.016 + (altitude - 1.0) * 0.010).coerceIn(0.016, 0.038)

                visualizationViewModel.setObjects(
                    listOf(
                        RenderObject(1, RenderObjectType.GLOBE, Vector3D(0.0, 0.0, 0.0), Vector3D(0.0, 0.0, 0.0), Vector3D(1.0, 1.0, 1.0), true, 0),
                        buildOrbitPath(sat),
                        RenderObject(2, RenderObjectType.SATELLITE, pos, Vector3D(0.0, 0.0, headingZDeg), Vector3D(satScale, satScale, satScale), true, 2),
                        RenderObject(3, RenderObjectType.UI_MARKER, pos, Vector3D(0.0, 0.0, headingZDeg), Vector3D(arrowLength, satScale * 0.9, satScale * 0.75), true, 3)
                    )
                )
                previous = pos
                delay(16)
            }
        }
    }

    private fun buildOrbitPath(satellite: Satellite): RenderObject {
        val meanMotion = satellite.tle.meanMotion.coerceAtLeast(0.1)
        val orbitRadius = when {
            meanMotion >= 11.0 -> 1.22
            meanMotion >= 2.0 -> 1.72
            else -> 2.28
        }

        // b = a * sqrt(1 - e^2), aproximando una elipse orbital a partir de TLE
        val eccentricity = satellite.tle.eccentricity.coerceIn(0.0, 0.95)
        val minorAxis = orbitRadius * sqrt(1.0 - eccentricity * eccentricity)

        return RenderObject(
            id = 4,
            type = RenderObjectType.ORBIT_PATH,
            position = Vector3D(0.0, 0.0, 0.0),
            rotation = Vector3D(
                satellite.tle.inclination,
                satellite.tle.argumentOfPerigee,
                satellite.tle.raan
            ),
            scale = Vector3D(orbitRadius, minorAxis, orbitRadius),
            isVisible = true,
            layer = 1
        )
    }
}
