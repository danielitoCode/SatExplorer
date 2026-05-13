package com.elitec.satexplorer.feature.visualization.domain.entity

import com.elitec.satexplorer.feature.tracking.domain.entity.Vector3D

data class RenderObject(
    val id: Long,
    val type: RenderObjectType,
    val position: Vector3D,
    val rotation: Vector3D,
    val scale: Vector3D,
    val isVisible: Boolean,
    val layer: Int
)
