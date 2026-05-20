package com.elitec.satexplorer.feature.tracking.domain.repository

import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite

interface SatelliteRepository {
    suspend fun fetchFromCelestrak(noradId: Int): Satellite
    suspend fun getCachedSatellite(noradId: Int): Satellite?
    fun fromManualInput(name: String, line1: String, line2: String): Satellite
}
