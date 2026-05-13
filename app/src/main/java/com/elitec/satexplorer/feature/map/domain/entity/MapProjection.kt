package com.elitec.satexplorer.feature.map.domain.entity

data class MapProjection(
    val width: Int,
    val height: Int,
    val centerLat: Double,
    val centerLon: Double,
    val zoom: Float
)