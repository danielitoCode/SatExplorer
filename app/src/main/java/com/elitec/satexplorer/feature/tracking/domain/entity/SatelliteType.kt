package com.elitec.satexplorer.feature.tracking.domain.entity

enum class SatelliteType {
    LEO,        // Low Earth Orbit
    MEO,        // Medium Earth Orbit
    GEO,        // Geostationary
    HEO,        // Highly Elliptical Orbit
    DEBRIS,     // basura espacial
    PAYLOAD,    // carga útil
    ISS,
    CONSTELLATION
}