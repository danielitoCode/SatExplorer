package com.elitec.satexplorer.feature.analitics.domain.entity

data class PerformanceMetric(
    val frameRate: Float,
    val cpuUsage: Float,
    val memoryUsage: Float,
    val renderTimeMs: Float
)
