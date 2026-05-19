package com.elitec.satexplorer.feature.auth.domain.entity

import com.elitec.satexplorer.infrastructure.domain.DistanceUnitsMetrics
import com.elitec.satexplorer.infrastructure.domain.VelocityUnitsMetrics

data class SystemSettingsConfiguration(
    val refreshRate: Float,
    val isAutoStabilized: Boolean,
    val distanceUnitsMetrics: DistanceUnitsMetrics = DistanceUnitsMetrics.KM,
    val velocityUnitsMetrics: VelocityUnitsMetrics
)
