package com.elitec.satexplorer.feature.satellite.domain.entity

import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite

data class SatelliteCatalogPage(
    val satellites: List<Satellite>,
    val page: Int,
    val pageSize: Int,
    val totalItems: Int,
    val hasNextPage: Boolean
)
