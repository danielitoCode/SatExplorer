package com.elitec.satexplorer.feature.satellite.domain.caseuse

import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteVisualDetails
import com.elitec.satexplorer.feature.satellite.domain.repository.SatelliteCatalogRepository

class LoadSatelliteVisualDetailsUseCase(
    private val repository: SatelliteCatalogRepository
) {
    suspend operator fun invoke(noradId: Int): SatelliteVisualDetails? {
        return repository.loadVisualDetails(noradId)
    }
}
