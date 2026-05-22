package com.elitec.satexplorer.feature.analitics.domain.caseuse

import com.elitec.satexplorer.feature.analitics.domain.entity.PerformanceMetric
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import kotlin.math.PI
import kotlin.random.Random

class TrackPerformanceUseCase {
    operator fun invoke(satellite: Satellite?, isLoading: Boolean): PerformanceMetric {
        val meanMotion = satellite?.tle?.meanMotion ?: 15.2
        val orbitalRadiusKm = estimateOrbitalRadiusKm(meanMotion)
        val orbitalVelocityKmH = ((2 * PI * orbitalRadiusKm) * meanMotion).toFloat()

        val renderTime = if (isLoading) 45f else 11f + (meanMotion % 6).toFloat()
        val cpuUsage = if (isLoading) 68f else (22f + (meanMotion % 12).toFloat())
        val memoryUsage = (orbitalVelocityKmH / 400f).coerceIn(25f, 85f)

        return PerformanceMetric(
            frameRate = (60f - renderTime).coerceIn(20f, 60f),
            cpuUsage = cpuUsage.coerceIn(10f, 95f),
            memoryUsage = memoryUsage,
            renderTimeMs = renderTime,
        )
    }

    private fun estimateOrbitalRadiusKm(meanMotion: Double): Double {
        val altitudeKm = when {
            meanMotion >= 14.0 -> 550.0
            meanMotion >= 11.0 -> 900.0
            meanMotion >= 2.0 -> 20000.0
            else -> 35786.0
        }
        return 6371.0 + altitudeKm
    }
}