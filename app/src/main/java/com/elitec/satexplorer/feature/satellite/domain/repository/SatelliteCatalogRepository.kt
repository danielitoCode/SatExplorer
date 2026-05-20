package com.elitec.satexplorer.feature.satellite.domain.repository

import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteCatalogPage
import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteVisualDetails

interface SatelliteCatalogRepository {
    suspend fun searchSatellites(
        query: String,
        page: Int,
        pageSize: Int
    ): SatelliteCatalogPage

    suspend fun loadVisualDetails(noradId: Int): SatelliteVisualDetails?
}
