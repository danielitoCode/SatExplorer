package com.elitec.satexplorer.feature.visualization.domain.entity

import com.elitec.satexplorer.feature.tracking.domain.entity.Vector3D

data class CameraState(
    val position: Vector3D,
    val target: Vector3D,
    val zoom: Float,
    val pitch: Float,
    val yaw: Float
)

