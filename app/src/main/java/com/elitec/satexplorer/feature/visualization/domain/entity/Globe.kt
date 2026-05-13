package com.elitec.satexplorer.feature.visualization.domain.entity

data class Globe(
    val radius: Float = 6371f,   // km (Tierra real)
    val textureDay: String,
    val textureNight: String,
    val rotationSpeed: Float
)
