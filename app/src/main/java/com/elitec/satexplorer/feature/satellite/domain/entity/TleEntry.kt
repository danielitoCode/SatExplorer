package com.elitec.satexplorer.feature.satellite.domain.entity

data class TleEntry(
    val noradId: Int,
    val name: String,
    val tleLine1: String,
    val tleLine2: String,
    val category: String,     // ISS, Starlink, debris, etc.
    val lastUpdated: Long
)
