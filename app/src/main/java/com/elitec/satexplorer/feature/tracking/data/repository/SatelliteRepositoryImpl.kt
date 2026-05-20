package com.elitec.satexplorer.feature.tracking.data.repository

import com.elitec.satexplorer.feature.tracking.data.mapper.TleMapper
import com.elitec.satexplorer.feature.tracking.data.remote.CelestrakRemoteDataSource
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import com.elitec.satexplorer.feature.tracking.domain.repository.SatelliteRepository
import com.elitec.satexplorer.infrastructure.data.local.SatelliteCacheLocalDataSource

class SatelliteRepositoryImpl(
    private val remote: CelestrakRemoteDataSource,
    private val mapper: TleMapper,
    private val localDataSource: SatelliteCacheLocalDataSource
) : SatelliteRepository {
    override suspend fun fetchFromCelestrak(noradId: Int): Satellite {
        return runCatching {
            mapper.toDomain(remote.fetchTle(noradId)).also {
                localDataSource.upsertOrbitSatellite(it)
            }
        }.getOrElse { error ->
            localDataSource.getSatellite(noradId) ?: throw error
        }
    }

    override suspend fun getCachedSatellite(noradId: Int): Satellite? = localDataSource.getSatellite(noradId)

    override fun fromManualInput(name: String, line1: String, line2: String): Satellite =
        mapper.fromManual(name, line1, line2)
}
