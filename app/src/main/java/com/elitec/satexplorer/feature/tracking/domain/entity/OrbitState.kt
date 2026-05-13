package com.elitec.satexplorer.feature.tracking.domain.entity

data class OrbitState(
    val position: Vector3D,   // km en espacio 3D
    val velocity: Vector3D,   // km/s
    val timestamp: Long
)