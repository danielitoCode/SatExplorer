package com.elitec.satexplorer.feature.prediction.domain.entity

data class PredictionResult(
    val visibilityProbability: Float, // 0–1
    val maxElevation: Float,          // grados
    val startTime: Long,
    val endTime: Long,
    val confidence: Float
)
