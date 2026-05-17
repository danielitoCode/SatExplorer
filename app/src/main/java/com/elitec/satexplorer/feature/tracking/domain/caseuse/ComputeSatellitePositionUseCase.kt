package com.elitec.satexplorer.feature.tracking.domain.caseuse

import com.elitec.satexplorer.feature.tracking.domain.entity.TleData
import com.elitec.satexplorer.feature.tracking.domain.entity.Vector3D
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ComputeSatellitePositionUseCase {
    operator fun invoke(tle: TleData, epochMillis: Long): Vector3D {
        val fields = tle.line2.trim().split(Regex("\\s+"))
        val inclinationDeg = fields.getOrNull(2)?.toDoubleOrNull() ?: 51.6
        val raanDeg = fields.getOrNull(3)?.toDoubleOrNull() ?: 0.0
        val meanMotion = fields.getOrNull(7)?.toDoubleOrNull() ?: 15.5

        val inclination = Math.toRadians(inclinationDeg)
        val raan = Math.toRadians(raanDeg)
        val angularSpeed = (meanMotion * 2.0 * PI) / (24.0 * 3600.0)
        val t = epochMillis / 1000.0
        val phase = t * angularSpeed

        val radius = 1.25
        val xOrbital = radius * cos(phase)
        val yOrbital = radius * sin(phase)

        val x = xOrbital * cos(raan) - yOrbital * sin(raan) * cos(inclination)
        val y = xOrbital * sin(raan) + yOrbital * cos(raan) * cos(inclination)
        val z = yOrbital * sin(inclination)

        return Vector3D(x, y, z)
    }
}