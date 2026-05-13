package com.elitec.satexplorer.feature.tracking.domain.entity

data class VelocityVector(
    val vx: Double,   // km/s
    val vy: Double,
    val vz: Double,
    val magnitude: Double
)
