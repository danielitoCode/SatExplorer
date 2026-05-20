package com.elitec.satexplorer.feature.satellite.domain.repository

import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteCatalogPage

interface SatelliteCatalogRepository {
    suspend fun searchSatellites(
        query: String,
        page: Int,
        pageSize: Int
    ): SatelliteCatalogPage
}
