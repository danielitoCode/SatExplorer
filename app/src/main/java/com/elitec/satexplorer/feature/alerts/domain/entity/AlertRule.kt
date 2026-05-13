package com.elitec.satexplorer.feature.alerts.domain.entity

data class AlertRule(
    val id: Long,
    val satelliteId: String?,
    val condition: String, // "visibility > 0.8"
    val enabled: Boolean
)
