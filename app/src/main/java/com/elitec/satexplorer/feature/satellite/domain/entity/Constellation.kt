package com.elitec.satexplorer.feature.satellite.domain.entity

data class Constellation(
    val id: Long,
    val name: String,
    val satellites: List<Int>, // NORAD IDs
    val owner: String
)
