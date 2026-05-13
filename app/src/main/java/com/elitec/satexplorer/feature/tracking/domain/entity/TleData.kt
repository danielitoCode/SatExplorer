package com.elitec.satexplorer.feature.tracking.domain.entity

data class TleData(
    val line1: String,
    val line2: String,
    val epoch: Double,
    val meanMotion: Double,
    val eccentricity: Double,
    val inclination: Double,
    val raan: Double,        // Right Ascension Ascending Node
    val argumentOfPerigee: Double,
    val meanAnomaly: Double
)
