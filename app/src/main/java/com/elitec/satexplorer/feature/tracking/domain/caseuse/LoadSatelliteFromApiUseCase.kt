package com.elitec.satexplorer.feature.tracking.domain.caseuse

import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import com.elitec.satexplorer.feature.tracking.domain.repository.SatelliteRepository

class LoadSatelliteFromApiUseCase(
    private val repository: SatelliteRepository
) {
    suspend operator fun invoke(noradId: Int): Satellite = repository.fetchFromCelestrak(noradId)
}