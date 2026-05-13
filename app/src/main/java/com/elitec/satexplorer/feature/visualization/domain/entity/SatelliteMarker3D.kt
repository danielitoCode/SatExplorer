package com.elitec.satexplorer.feature.visualization.domain.entity

import com.elitec.satexplorer.feature.tracking.domain.entity.Vector3D

data class SatelliteMarker3D(
    val satelliteId: String,
    val position: Vector3D,
    val screenPosition: Pair<Float, Float>?,
    val isVisible: Boolean
)
