package com.elitec.satexplorer.feature.satellite.presentation.model

import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteCatalogCategory
import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteVisualDetails
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite

data class SatelliteSearchUiState(
    val query: String = "",
    val selectedCategory: SatelliteCatalogCategory = SatelliteCatalogCategory.ALL,
    val satellites: List<Satellite> = emptyList(),
    val visualDetailsByNorad: Map<Int, SatelliteVisualDetails> = emptyMap(),
    val loadingVisuals: Set<Int> = emptySet(),
    val totalItems: Int = 0,
    val currentPage: Int = 0,
    val pageSize: Int = 12,
    val hasNextPage: Boolean = false,
    val isLoading: Boolean = false,
    val isAppending: Boolean = false,
    val errorMessage: String? = null
)
