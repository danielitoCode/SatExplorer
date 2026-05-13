package com.elitec.satexplorer.feature.map.domain.entity

data class GroundTrack(
    val satelliteId: String,
    val points: List<GeoCoordinate>,
    val timestampStart: Long,
    val timestampEnd: Long
)
