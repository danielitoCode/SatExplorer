package com.elitec.satexplorer.feature.tracking.data.mapper

import com.elitec.satexplorer.feature.tracking.data.dto.CelestrakTleDto
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import com.elitec.satexplorer.feature.tracking.domain.entity.SatelliteType
import com.elitec.satexplorer.feature.tracking.domain.entity.TleData

class TleMapper {

    fun toDomain(dto: CelestrakTleDto): Satellite = mapToSatellite(
        name = dto.name,
        line1 = dto.line1,
        line2 = dto.line2
    )

    fun fromManual(name: String, line1: String, line2: String): Satellite = mapToSatellite(
        name = name,
        line1 = line1,
        line2 = line2
    )

    private fun mapToSatellite(name: String, line1: String, line2: String): Satellite {
        val fields = line2.trim().split(Regex("\\s+")).filter { it.isNotBlank() }

        val noradId = line1.substring(2, 7)?.trim()?.toIntOrNull() ?: 0
        val inclination = fields.getOrNull(2)?.toDoubleOrNull() ?: 0.0
        val raan = fields.getOrNull(3)?.toDoubleOrNull() ?: 0.0
        val eccentricity = fields.getOrNull(4)?.let { "0.$it" }?.toDoubleOrNull() ?: 0.0
        val argumentOfPerigee = fields.getOrNull(5)?.toDoubleOrNull() ?: 0.0
        val meanAnomaly = fields.getOrNull(6)?.toDoubleOrNull() ?: 0.0
        val meanMotion = fields.getOrNull(7)?.toDoubleOrNull() ?: 0.0

        val tle = TleData(
            line1 = line1,
            line2 = line2,
            epoch = 0,
            meanMotion = meanMotion,
            eccentricity = eccentricity,
            inclination = inclination,
            raan = raan,
            argumentOfPerigee = argumentOfPerigee,
            meanAnomaly = meanAnomaly
        )

        return Satellite(
            id = 0L,
            noradId = noradId,
            name = name,
            type = SatelliteType.PAYLOAD,
            tle = tle,
            launchDate = null,
            isActive = true
        )
    }
}