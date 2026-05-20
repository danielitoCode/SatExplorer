package com.elitec.satexplorer.feature.satellite.domain.caseuse

import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteCatalogPage
import com.elitec.satexplorer.feature.satellite.domain.repository.SatelliteCatalogRepository

class SyncTleUseCase(
    private val repository: SatelliteCatalogRepository
) {
    suspend operator fun invoke(
        query: String,
        page: Int,
        pageSize: Int
    ): SatelliteCatalogPage {
        return repository.searchSatellites(
            query = query,
            page = page,
            pageSize = pageSize
        )
    }
}
