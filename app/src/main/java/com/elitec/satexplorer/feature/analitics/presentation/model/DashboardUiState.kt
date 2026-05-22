package com.elitec.satexplorer.feature.analitics.presentation.model

data class DashboardUiState(
    val trackedSatellites: Int,
    val visibleTonight: Int,
    val averageVelocityKmh: Int,
    val congestionAlert: String,
    val proximityAlert: ProximityAlertUi?,
    val logs: List<LiveLogItem>,
    val apiResponsivenessMs: Int,
    val apiHealthPercent: Float,
)