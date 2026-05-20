package com.elitec.satexplorer.feature.satellite.data.mapper

import com.elitec.satexplorer.feature.satellite.data.dto.TleCatalogItemDto
import com.elitec.satexplorer.feature.satellite.data.dto.TleCatalogResponseDto
import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteCatalogPage
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import com.elitec.satexplorer.feature.tracking.domain.entity.SatelliteType
import com.elitec.satexplorer.feature.tracking.domain.entity.TleData
import java.time.OffsetDateTime

class SatelliteCatalogMapper {

    fun toDomain(dto: TleCatalogResponseDto): SatelliteCatalogPage {
        return SatelliteCatalogPage(
            satellites = dto.member.map(::toSatellite),
            page = dto.parameters.page,
            pageSize = dto.parameters.pageSize,
            totalItems = dto.totalItems,
            hasNextPage = dto.view?.next != null
        )
    }

    private fun toSatellite(dto: TleCatalogItemDto): Satellite {
        val fields = dto.line2.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
        val inclination = fields.getOrNull(2)?.toDoubleOrNull() ?: 0.0
        val raan = fields.getOrNull(3)?.toDoubleOrNull() ?: 0.0
        val eccentricity = fields.getOrNull(4)?.let { "0.$it" }?.toDoubleOrNull() ?: 0.0
        val argumentOfPerigee = fields.getOrNull(5)?.toDoubleOrNull() ?: 0.0
        val meanAnomaly = fields.getOrNull(6)?.toDoubleOrNull() ?: 0.0
        val meanMotion = fields.getOrNull(7)?.toDoubleOrNull() ?: 0.0
        val tleEpoch = runCatching { OffsetDateTime.parse(dto.date).toInstant().toEpochMilli() }
            .getOrDefault(0L)

        return Satellite(
            id = dto.satelliteId.toLong(),
            noradId = dto.satelliteId,
            name = dto.name,
            type = resolveType(dto.name, meanMotion),
            tle = TleData(
                line1 = dto.line1,
                line2 = dto.line2,
                epoch = tleEpoch,
                meanMotion = meanMotion,
                eccentricity = eccentricity,
                inclination = inclination,
                raan = raan,
                argumentOfPerigee = argumentOfPerigee,
                meanAnomaly = meanAnomaly
            ),
            launchDate = tleEpoch,
            isActive = true
        )
    }

    private fun resolveType(name: String, meanMotion: Double): SatelliteType {
        val normalizedName = name.lowercase()

        return when {
            normalizedName.contains("iss") -> SatelliteType.ISS
            normalizedName.contains("debris") || normalizedName.contains("object") -> SatelliteType.DEBRIS
            normalizedName.contains("starlink") ||
                normalizedName.contains("oneweb") ||
                normalizedName.contains("iridium") ||
                normalizedName.contains("planet") -> SatelliteType.CONSTELLATION
            meanMotion >= 11.0 -> SatelliteType.LEO
            meanMotion >= 2.0 -> SatelliteType.MEO
            meanMotion > 0.0 -> SatelliteType.GEO
            else -> SatelliteType.PAYLOAD
        }
    }
}
