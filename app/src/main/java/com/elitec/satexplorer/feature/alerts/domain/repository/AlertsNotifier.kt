package com.elitec.satexplorer.feature.alerts.domain.repository

interface AlertsNotifier {
    fun scheduleClosestSatelliteNotification(satelliteName: String, tMinus: String)
}