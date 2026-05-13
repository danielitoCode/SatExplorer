package com.elitec.satexplorer.feature.prediction.domain.entity

data class VisibilityScore(
    val satelliteId: String,
    val score: Float,     // 0–100
    val reason: String
)
