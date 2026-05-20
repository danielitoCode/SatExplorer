package com.elitec.satexplorer.feature.satellite.data.repository

import com.elitec.satexplorer.feature.satellite.data.mapper.SatelliteCatalogMapper
import com.elitec.satexplorer.feature.satellite.data.remote.TleCatalogRemoteDataSource
import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteCatalogPage
import com.elitec.satexplorer.feature.satellite.domain.repository.SatelliteCatalogRepository

class SatelliteCatalogRepositoryImpl(
    private val remoteDataSource: TleCatalogRemoteDataSource,
    private val mapper: SatelliteCatalogMapper
) : SatelliteCatalogRepository {

    override suspend fun searchSatellites(
        query: String,
        page: Int,
        pageSize: Int
    ): SatelliteCatalogPage {
        return mapper.toDomain(remoteDataSource.searchSatellites(query, page, pageSize))
    }
}
