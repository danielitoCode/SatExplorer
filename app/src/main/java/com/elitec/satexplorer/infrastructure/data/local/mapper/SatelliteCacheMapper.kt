package com.elitec.satexplorer.infrastructure.data.local.mapper

import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteVisualDetails
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import com.elitec.satexplorer.feature.tracking.domain.entity.SatelliteType
import com.elitec.satexplorer.feature.tracking.domain.entity.TleData
import com.elitec.satexplorer.infrastructure.data.local.entity.SatelliteCacheEntity

class SatelliteCacheMapper {

    fun toDomain(entity: SatelliteCacheEntity): Satellite {
        return Satellite(
            id = entity.noradId.toLong(),
            noradId = entity.noradId,
            name = entity.name,
            type = entity.type.toSatelliteType(),
            tle = TleData(
                line1 = entity.line1,
                line2 = entity.line2,
                epoch = entity.epoch,
                meanMotion = entity.meanMotion,
                eccentricity = entity.eccentricity,
                inclination = entity.inclination,
                raan = entity.raan,
                argumentOfPerigee = entity.argumentOfPerigee,
                meanAnomaly = entity.meanAnomaly
            ),
            launchDate = entity.launchDate,
            isActive = entity.isActive
        )
    }

    fun toVisualDetails(entity: SatelliteCacheEntity): SatelliteVisualDetails? {
        if (entity.imageUrl == null && entity.website == null && entity.status == null && entity.countries == null) {
            return null
        }

        return SatelliteVisualDetails(
            imageUrl = entity.imageUrl,
            website = entity.website,
            status = entity.status,
            countries = entity.countries
        )
    }

    fun fromSatellite(
        satellite: Satellite,
        previous: SatelliteCacheEntity? = null,
        syncedAt: Long = System.currentTimeMillis(),
        syncOrbit: Boolean = false,
        syncCatalog: Boolean = false
    ): SatelliteCacheEntity {
        return SatelliteCacheEntity(
            noradId = satellite.noradId,
            name = satellite.name,
            type = satellite.type.name,
            line1 = satellite.tle.line1,
            line2 = satellite.tle.line2,
            epoch = satellite.tle.epoch,
            meanMotion = satellite.tle.meanMotion,
            eccentricity = satellite.tle.eccentricity,
            inclination = satellite.tle.inclination,
            raan = satellite.tle.raan,
            argumentOfPerigee = satellite.tle.argumentOfPerigee,
            meanAnomaly = satellite.tle.meanAnomaly,
            launchDate = satellite.launchDate,
            isActive = satellite.isActive,
            imageUrl = previous?.imageUrl,
            website = previous?.website,
            status = previous?.status,
            countries = previous?.countries,
            lastCatalogSync = if (syncCatalog) syncedAt else previous?.lastCatalogSync,
            lastVisualSync = previous?.lastVisualSync,
            lastOrbitSync = if (syncOrbit) syncedAt else previous?.lastOrbitSync
        )
    }

    fun mergeVisualDetails(
        previous: SatelliteCacheEntity,
        details: SatelliteVisualDetails,
        syncedAt: Long = System.currentTimeMillis()
    ): SatelliteCacheEntity {
        return previous.copy(
            imageUrl = details.imageUrl ?: previous.imageUrl,
            website = details.website ?: previous.website,
            status = details.status ?: previous.status,
            countries = details.countries ?: previous.countries,
            lastVisualSync = syncedAt
        )
    }

    private fun String.toSatelliteType(): SatelliteType {
        return SatelliteType.entries.firstOrNull { it.name == this } ?: SatelliteType.PAYLOAD
    }
}
