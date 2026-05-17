package com.elitec.satexplorer.feature.tracking.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CelestrakTleDto(
    val name: String,
    val line1: String,
    val line2: String
)