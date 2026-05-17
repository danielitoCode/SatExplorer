package com.elitec.satexplorer.feature.tracking.domain.caseuse

import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import com.elitec.satexplorer.feature.tracking.domain.repository.SatelliteRepository

class LoadSatelliteManualUseCase(
    private val repository: SatelliteRepository
) {
    operator fun invoke(name: String, line1: String, line2: String): Satellite =
        repository.fromManualInput(name, line1, line2)
}