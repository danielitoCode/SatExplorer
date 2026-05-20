package com.elitec.satexplorer.feature.satellite.data.repository

import com.elitec.satexplorer.feature.satellite.data.mapper.SatelliteCatalogMapper
import com.elitec.satexplorer.feature.satellite.data.remote.SatNogsRemoteDataSource
import com.elitec.satexplorer.feature.satellite.data.remote.TleCatalogRemoteDataSource
import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteCatalogCategory
import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteCatalogPage
import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteVisualDetails
import com.elitec.satexplorer.feature.satellite.domain.repository.SatelliteCatalogRepository
import com.elitec.satexplorer.infrastructure.data.local.SatelliteCacheLocalDataSource

class SatelliteCatalogRepositoryImpl(
    private val remoteDataSource: TleCatalogRemoteDataSource,
    private val satNogsRemoteDataSource: SatNogsRemoteDataSource,
    private val localDataSource: SatelliteCacheLocalDataSource,
    private val mapper: SatelliteCatalogMapper
) : SatelliteCatalogRepository {

    override suspend fun getCachedSatellites(
        query: String,
        page: Int,
        pageSize: Int,
        category: SatelliteCatalogCategory
    ): SatelliteCatalogPage {
        return localDataSource.getCatalogPage(
            query = query,
            page = page,
            pageSize = pageSize,
            category = category
        )
    }

    override suspend fun searchSatellites(
        query: String,
        page: Int,
        pageSize: Int,
        category: SatelliteCatalogCategory
    ): SatelliteCatalogPage {
        val cachedPage = getCachedSatellites(query, page, pageSize, category)

        return runCatching {
            val remotePage = mapper.toDomain(
                remoteDataSource.searchSatellites(
                    buildRemoteQuery(query, category),
                    page,
                    pageSize
                )
            )
            localDataSource.upsertCatalogSatellites(remotePage.satellites)
            localDataSource.getCatalogPage(query, page, pageSize, category)
        }.getOrElse {
            if (cachedPage.satellites.isNotEmpty()) cachedPage else throw it
        }
    }

    override suspend fun loadVisualDetails(noradId: Int): SatelliteVisualDetails? {
        localDataSource.getVisualDetails(noradId)?.let { return it }

        val remoteDetails = mapper.toVisualDetails(satNogsRemoteDataSource.fetchByNorad(noradId))
        if (remoteDetails != null) {
            localDataSource.upsertVisualDetails(noradId, remoteDetails)
        }
        return remoteDetails
    }

    private fun buildRemoteQuery(query: String, category: SatelliteCatalogCategory): String {
        return listOf(category.presetQuery.trim(), query.trim())
            .filter { it.isNotBlank() }
            .joinToString(" ")
    }
}
