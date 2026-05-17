package com.elitec.satexplorer.feature.tracking.presentation.model

import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite

data class SatelliteUiState(
    val isLoading: Boolean = false,
    val satellite: Satellite? = null,
    val error: String? = null
)