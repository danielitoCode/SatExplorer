package com.elitec.satexplorer.feature.satellite.domain.repository

import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteCatalogPage
import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteCatalogCategory
import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteVisualDetails

interface SatelliteCatalogRepository {
    suspend fun getCachedSatellites(
        query: String,
        page: Int,
        pageSize: Int,
        category: SatelliteCatalogCategory
    ): SatelliteCatalogPage

    suspend fun searchSatellites(
        query: String,
        page: Int,
        pageSize: Int,
        category: SatelliteCatalogCategory
    ): SatelliteCatalogPage

    suspend fun loadVisualDetails(noradId: Int): SatelliteVisualDetails?
}
