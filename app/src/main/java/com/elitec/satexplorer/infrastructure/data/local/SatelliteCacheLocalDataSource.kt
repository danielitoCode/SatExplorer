package com.elitec.satexplorer.infrastructure.data.local

import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteCatalogCategory
import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteCatalogPage
import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteVisualDetails
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import com.elitec.satexplorer.infrastructure.data.local.dao.SatelliteCacheDao
import com.elitec.satexplorer.infrastructure.data.local.mapper.SatelliteCacheMapper

class SatelliteCacheLocalDataSource(
    private val dao: SatelliteCacheDao,
    private val mapper: SatelliteCacheMapper
) {

    suspend fun getCatalogPage(
        query: String,
        page: Int,
        pageSize: Int,
        category: SatelliteCatalogCategory
    ): SatelliteCatalogPage {
        val offset = ((page - 1).coerceAtLeast(0)) * pageSize
        val typeNames = category.toTypeNames()
        val items = if (typeNames == null) {
            dao.searchAll(query.trim(), pageSize, offset)
        } else {
            dao.searchByTypes(query.trim(), typeNames, pageSize, offset)
        }
        val totalItems = if (typeNames == null) {
            dao.countAll(query.trim())
        } else {
            dao.countByTypes(query.trim(), typeNames)
        }

        return SatelliteCatalogPage(
            satellites = items.map(mapper::toDomain),
            page = page,
            pageSize = pageSize,
            totalItems = totalItems,
            hasNextPage = offset + items.size < totalItems
        )
    }

    suspend fun getSatellite(noradId: Int): Satellite? {
        return dao.getByNorad(noradId)?.let(mapper::toDomain)
    }

    suspend fun getVisualDetails(noradId: Int): SatelliteVisualDetails? {
        return dao.getByNorad(noradId)?.let(mapper::toVisualDetails)
    }

    suspend fun upsertCatalogSatellites(satellites: List<Satellite>) {
        val syncedAt = System.currentTimeMillis()
        val entities = satellites.map { satellite ->
            mapper.fromSatellite(
                satellite = satellite,
                previous = dao.getByNorad(satellite.noradId),
                syncedAt = syncedAt,
                syncCatalog = true
            )
        }
        dao.upsertAll(entities)
    }

    suspend fun upsertOrbitSatellite(satellite: Satellite) {
        val existing = dao.getByNorad(satellite.noradId)
        dao.upsert(
            mapper.fromSatellite(
                satellite = satellite,
                previous = existing,
                syncedAt = System.currentTimeMillis(),
                syncOrbit = true,
                syncCatalog = existing == null
            )
        )
    }

    suspend fun upsertVisualDetails(noradId: Int, details: SatelliteVisualDetails) {
        val existing = dao.getByNorad(noradId) ?: return
        dao.upsert(mapper.mergeVisualDetails(existing, details))
    }

    private fun SatelliteCatalogCategory.toTypeNames(): List<String>? {
        return when (this) {
            SatelliteCatalogCategory.ALL -> null
            SatelliteCatalogCategory.STATION -> listOf("ISS")
            SatelliteCatalogCategory.LEO -> listOf("LEO")
            SatelliteCatalogCategory.MEO -> listOf("MEO")
            SatelliteCatalogCategory.GEO -> listOf("GEO")
            SatelliteCatalogCategory.HEO -> listOf("HEO")
            SatelliteCatalogCategory.CONSTELLATION -> listOf("CONSTELLATION")
            SatelliteCatalogCategory.DEBRIS -> listOf("DEBRIS")
        }
    }
}
