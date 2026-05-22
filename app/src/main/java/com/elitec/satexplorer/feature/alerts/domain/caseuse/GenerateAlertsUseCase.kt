package com.elitec.satexplorer.feature.alerts.domain.caseuse

import com.elitec.satexplorer.feature.alerts.domain.entity.Alert
import com.elitec.satexplorer.feature.alerts.domain.entity.AlertPriority
import com.elitec.satexplorer.feature.alerts.domain.entity.AlertType
import com.elitec.satexplorer.feature.analitics.domain.entity.PerformanceMetric
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite

class GenerateAlertsUseCase {
    operator fun invoke(satellite: Satellite?, metric: PerformanceMetric): List<Alert> {
        if (satellite == null) return emptyList()

        val passInMinutes = ((90.0 / satellite.tle.meanMotion).coerceIn(5.0, 120.0)).toLong()
        val triggerAt = System.currentTimeMillis() + passInMinutes * 60 * 1000L

        return listOf(
            Alert(
                id = "pass-${satellite.noradId}",
                satelliteId = satellite.name,
                type = AlertType.SATELLITE_PASS_START,
                message = "${satellite.name} visible pass window detected",
                triggerTime = triggerAt,
                isRead = false,
                priority = if (metric.renderTimeMs > 35f) AlertPriority.HIGH else AlertPriority.MEDIUM
            )
        )
    }
}