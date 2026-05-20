package com.elitec.satexplorer.infrastructure.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "satellite_cache")
data class SatelliteCacheEntity(
    @PrimaryKey val noradId: Int,
    val name: String,
    val type: String,
    val line1: String,
    val line2: String,
    val epoch: Long,
    val meanMotion: Double,
    val eccentricity: Double,
    val inclination: Double,
    val raan: Double,
    val argumentOfPerigee: Double,
    val meanAnomaly: Double,
    val launchDate: Long?,
    val isActive: Boolean,
    val imageUrl: String?,
    val website: String?,
    val status: String?,
    val countries: String?,
    val lastCatalogSync: Long?,
    val lastVisualSync: Long?,
    val lastOrbitSync: Long?
)
