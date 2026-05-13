package com.elitec.satexplorer.feature.alerts.domain.entity

data class Alert(
    val id: String,
    val satelliteId: String?,
    val type: AlertType,
    val message: String,
    val triggerTime: Long,
    val isRead: Boolean,
    val priority: AlertPriority
)
