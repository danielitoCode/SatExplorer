package com.elitec.satexplorer.feature.satellite.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SatNogsSatelliteDto(
    val norad_cat_id: Int,
    val image: String = "",
    val website: String = "",
    val status: String = "",
    val countries: String = ""
)
