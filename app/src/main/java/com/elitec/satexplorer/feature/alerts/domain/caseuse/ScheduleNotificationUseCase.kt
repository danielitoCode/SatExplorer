package com.elitec.satexplorer.feature.alerts.domain.caseuse

import com.elitec.satexplorer.feature.alerts.domain.repository.AlertsNotifier

class ScheduleNotificationUseCase(
    private val alertsNotifier: AlertsNotifier
) {
    operator fun invoke(satelliteName: String, tMinus: String): String {
        alertsNotifier.scheduleClosestSatelliteNotification(satelliteName, tMinus)
        return "Notification scheduled for $satelliteName at T-$tMinus"
    }
}