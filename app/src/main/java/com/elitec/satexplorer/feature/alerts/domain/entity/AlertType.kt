package com.elitec.satexplorer.feature.alerts.domain.entity

enum class AlertType {
    SATELLITE_PASS_START,
    SATELLITE_PASS_END,
    HIGH_VISIBILITY_WINDOW,
    LOW_VISIBILITY,
    ORBIT_ANOMALY,
    SYSTEM_ERROR,
    CUSTOM_RULE
}