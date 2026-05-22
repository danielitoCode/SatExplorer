package com.elitec.satexplorer.feature.analitics.presentation.viewmodel

import com.elitec.satexplorer.feature.analitics.presentation.model.DashboardUiState
import com.elitec.satexplorer.feature.analitics.presentation.model.LiveLogItem
import com.elitec.satexplorer.feature.analitics.presentation.model.LogType
import com.elitec.satexplorer.feature.analitics.presentation.model.ProximityAlertUi
import androidx.lifecycle.ViewModel
import com.elitec.satexplorer.feature.alerts.domain.caseuse.GenerateAlertsUseCase
import com.elitec.satexplorer.feature.alerts.domain.caseuse.ScheduleNotificationUseCase
import com.elitec.satexplorer.feature.analitics.domain.caseuse.LogEventUseCase
import com.elitec.satexplorer.feature.analitics.domain.caseuse.TrackPerformanceUseCase
import com.elitec.satexplorer.feature.analitics.domain.entity.EventLog
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import com.elitec.satexplorer.feature.tracking.presentation.model.SatelliteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardViewModel(
    private val trackPerformanceUseCase: TrackPerformanceUseCase,
    private val logEventUseCase: LogEventUseCase,
    private val generateAlertsUseCase: GenerateAlertsUseCase,
    private val scheduleNotificationUseCase: ScheduleNotificationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(buildUiState(SatelliteUiState()))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun syncWithTrackingState(trackingState: SatelliteUiState) {
        _uiState.value = buildUiState(trackingState)
    }

    fun notifyOnPass() {
        _uiState.value.proximityAlert?.let { alert ->
            val result = scheduleNotificationUseCase(alert.satelliteName, alert.tMinus)
            logEventUseCase("NOTIFY_ON_PASS", result)
            _uiState.value = _uiState.value.copy(logs = logEventUseCase.recentEvents().toLiveLogs())
        }
    }

    private fun buildUiState(trackingState: SatelliteUiState): DashboardUiState {
        val satellite = trackingState.satellite
        val metrics = trackPerformanceUseCase(satellite = satellite, isLoading = trackingState.isLoading)
        val alerts = generateAlertsUseCase(satellite, metrics)

        if (trackingState.error != null) {
            logEventUseCase("SYSTEM_ERROR", trackingState.error)
        } else if (satellite != null) {
            logEventUseCase("ORBIT_LOCKED", "Tracking ${satellite.name} (${satellite.noradId})")
        }

        val proximity = alerts.firstOrNull()?.let {
            ProximityAlertUi(
                satelliteName = it.satelliteId ?: satellite?.name ?: "Unknown satellite",
                tMinus = formatTMinus(it.triggerTime),
                azimuth = "${"%.2f".format(Locale.US, satellite?.tle?.raan ?: 0.0)}°",
                elevation = "+ ${"%.1f".format(Locale.US, satellite?.tle?.inclination ?: 0.0)}°"
            )
        }

        val health = if (trackingState.error == null) ((100f - metrics.cpuUsage) / 100f) else 0.15f
        return DashboardUiState(
            trackedSatellites = if (satellite != null) 1 else 0,
            visibleTonight = alerts.size,
            averageVelocityKmh = (metrics.memoryUsage * 400).toInt(),
            congestionAlert = when {
                trackingState.error != null -> "CRITICAL"
                metrics.renderTimeMs > 32f -> "HIGH"
                metrics.renderTimeMs > 20f -> "MEDIUM"
                else -> "LOW"
            },
            proximityAlert = proximity,
            logs = logEventUseCase.recentEvents().toLiveLogs(),
            apiResponsivenessMs = metrics.renderTimeMs.toInt().coerceAtLeast(1),
            apiHealthPercent = health.coerceIn(0.05f, 1f)
        )
    }

    private fun formatTMinus(triggerTime: Long): String {
        val diffSeconds = ((triggerTime - System.currentTimeMillis()) / 1000).coerceAtLeast(0)
        val minutes = diffSeconds / 60
        val seconds = diffSeconds % 60
        return "%02d:%02d".format(Locale.US, minutes, seconds)
    }
}

private fun List<EventLog>.toLiveLogs(): List<LiveLogItem> =
    map { event ->
        LiveLogItem(
            id = event.id,
            tittle = event.type,
            body = event.metadata,
            type = event.type.toLogType(),
            time = SimpleDateFormat("HH:mm", Locale.US).format(Date(event.timestamp))
        )
    }

private fun String.toLogType(): LogType = when {
    contains("ERROR", true) -> LogType.SIGNAL_LOST
    contains("LOCK", true) -> LogType.ORBIT_LOCKED
    contains("LOW", true) -> LogType.LOW_ALTITUDE
    contains("TIME", true) -> LogType.TIME_OUT
    else -> LogType.LIVE
}