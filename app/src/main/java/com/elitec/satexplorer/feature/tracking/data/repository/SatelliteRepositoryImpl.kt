package com.elitec.satexplorer.feature.tracking.data.repository

import com.elitec.satexplorer.feature.tracking.data.mapper.TleMapper
import com.elitec.satexplorer.feature.tracking.data.remote.CelestrakRemoteDataSource
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import com.elitec.satexplorer.feature.tracking.domain.repository.SatelliteRepository

class SatelliteRepositoryImpl(
    private val remote: CelestrakRemoteDataSource,
    private val mapper: TleMapper
) : SatelliteRepository {
    override suspend fun fetchFromCelestrak(noradId: Int): Satellite = mapper.toDomain(remote.fetchTle(noradId))

    override fun fromManualInput(name: String, line1: String, line2: String): Satellite =
        mapper.fromManual(name, line1, line2)
}