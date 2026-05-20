package com.elitec.satexplorer.feature.satellite.presentation.model

import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import com.elitec.satexplorer.feature.tracking.domain.entity.SatelliteType

data class SatelliteSearchUiState(
    val query: String = "",
    val selectedType: SatelliteType? = null,
    val satellites: List<Satellite> = emptyList(),
    val totalItems: Int = 0,
    val currentPage: Int = 0,
    val pageSize: Int = 20,
    val hasNextPage: Boolean = false,
    val isLoading: Boolean = false,
    val isAppending: Boolean = false,
    val errorMessage: String? = null
)
