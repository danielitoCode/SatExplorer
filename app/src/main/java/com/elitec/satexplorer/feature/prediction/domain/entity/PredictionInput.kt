package com.elitec.satexplorer.feature.prediction.domain.entity

data class PredictionInput(
    val satelliteId: String,
    val observerLat: Double,
    val observerLon: Double,
    val timeWindowMinutes: Int,
    val solarActivityIndex: Float
)
