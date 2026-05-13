package com.elitec.satexplorer.feature.settings.domain.entity

data class Preferences(
    val darkMode: Boolean,
    val notificationsEnabled: Boolean,
    val defaultZoom: Float,
    val units: UnitsSystem
)
