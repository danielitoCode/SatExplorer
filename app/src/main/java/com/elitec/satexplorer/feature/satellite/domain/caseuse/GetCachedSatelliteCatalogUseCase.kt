package com.elitec.satexplorer.feature.satellite.domain.caseuse

import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteCatalogCategory
import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteCatalogPage
import com.elitec.satexplorer.feature.satellite.domain.repository.SatelliteCatalogRepository

class GetCachedSatelliteCatalogUseCase(
    private val repository: SatelliteCatalogRepository
) {
    suspend operator fun invoke(
        query: String,
        page: Int,
        pageSize: Int,
        category: SatelliteCatalogCategory
    ): SatelliteCatalogPage {
        return repository.getCachedSatellites(query, page, pageSize, category)
    }
}
