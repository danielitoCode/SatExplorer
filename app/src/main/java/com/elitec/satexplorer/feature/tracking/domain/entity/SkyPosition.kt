package com.elitec.satexplorer.feature.tracking.domain.entity

data class SkyPosition(
    val azimuth: Float,       // 0..360 grados
    val elevation: Float,     // 0..90 grados
    val name: String,
    val nextPassTime: String  // Formato: "14:32 (Visible por 6 min)"
)