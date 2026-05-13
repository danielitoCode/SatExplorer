package com.elitec.satexplorer.feature.tracking.domain.entity

data class Satellite(
    val id: Long,
    val noradId: Int,            // ID oficial NORAD
    val name: String,            // Nombre del satélite
    val type: SatelliteType,     // LEO, GEO, MEO, etc.
    val tle: TleData,            // Datos orbitales base
    val launchDate: Long?,       // Epoch timestamp
    val isActive: Boolean
)
