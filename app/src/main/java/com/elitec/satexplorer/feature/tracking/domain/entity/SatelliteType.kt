package com.elitec.satexplorer.feature.tracking.domain.entity

import kotlin.random.Random
import kotlin.random.nextInt

enum class SatelliteType {
    LEO,        // Low Earth Orbit
    MEO,        // Medium Earth Orbit
    GEO,        // Geostationary
    HEO,        // Highly Elliptical Orbit
    DEBRIS,     // basura espacial
    PAYLOAD,    // carga útil
    ISS,
    CONSTELLATION;

    companion object {
        fun toList(): List<SatelliteType> =
            listOf(
                LEO,
                MEO,
                GEO,
                HEO,
                DEBRIS,
                PAYLOAD,
                ISS,
                CONSTELLATION
            )

        fun getRandomType(): SatelliteType =
            this.toList()[Random.nextInt(0..<this.toList().size)]

    }
}
