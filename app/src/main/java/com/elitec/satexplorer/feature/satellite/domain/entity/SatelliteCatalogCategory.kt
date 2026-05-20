package com.elitec.satexplorer.feature.satellite.domain.entity

import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import com.elitec.satexplorer.feature.tracking.domain.entity.SatelliteType

enum class SatelliteCatalogCategory(
    val label: String,
    val presetQuery: String
) {
    ALL("All", ""),
    STATION("Station", "iss"),
    LEO("LEO", ""),
    MEO("MEO", "gps"),
    GEO("GEO", "goes"),
    HEO("HEO", "molniya"),
    CONSTELLATION("Constellation", "starlink"),
    DEBRIS("Debris", "debris");

    fun matches(satellite: Satellite): Boolean {
        return when (this) {
            ALL -> true
            STATION -> satellite.type == SatelliteType.ISS
            LEO -> satellite.type == SatelliteType.LEO
            MEO -> satellite.type == SatelliteType.MEO
            GEO -> satellite.type == SatelliteType.GEO
            HEO -> satellite.type == SatelliteType.HEO
            CONSTELLATION -> satellite.type == SatelliteType.CONSTELLATION
            DEBRIS -> satellite.type == SatelliteType.DEBRIS
        }
    }
}
