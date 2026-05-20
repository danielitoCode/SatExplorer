package com.elitec.satexplorer.feature.satellite.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elitec.satexplorer.feature.satellite.domain.caseuse.FilterSatellitesUseCase
import com.elitec.satexplorer.feature.satellite.domain.caseuse.GetCachedSatelliteCatalogUseCase
import com.elitec.satexplorer.feature.satellite.domain.caseuse.LoadSatelliteVisualDetailsUseCase
import com.elitec.satexplorer.feature.satellite.domain.caseuse.SyncTleUseCase
import com.elitec.satexplorer.feature.satellite.domain.entity.SatelliteCatalogCategory
import com.elitec.satexplorer.feature.satellite.presentation.model.SatelliteSearchUiState
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SatelliteSearchViewModel(
    private val getCachedSatelliteCatalogUseCase: GetCachedSatelliteCatalogUseCase,
    private val syncTleUseCase: SyncTleUseCase,
    private val filterSatellitesUseCase: FilterSatellitesUseCase,
    private val loadSatelliteVisualDetailsUseCase: LoadSatelliteVisualDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SatelliteSearchUiState())
    val uiState = _uiState.asStateFlow()

    private var loadedSatellites: List<Satellite> = emptyList()

    init {
        submitSearch()
    }

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        applyFilters()
    }

    fun onCategorySelected(category: SatelliteCatalogCategory) {
        if (_uiState.value.selectedCategory == category) {
            return
        }

        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            visualDetailsByNorad = emptyMap(),
            loadingVisuals = emptySet(),
            errorMessage = null
        )
        submitSearch()
    }

    fun submitSearch() {
        loadPage(page = 1, append = false)
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoading || state.isAppending || !state.hasNextPage) {
            return
        }

        loadPage(page = state.currentPage + 1, append = true)
    }

    fun loadVisualDetailsIfNeeded(noradId: Int) {
        val state = _uiState.value
        if (noradId in state.visualDetailsByNorad || noradId in state.loadingVisuals) {
            return
        }

        _uiState.value = state.copy(loadingVisuals = state.loadingVisuals + noradId)
        viewModelScope.launch {
            val details = runCatching { loadSatelliteVisualDetailsUseCase(noradId) }.getOrNull()
            val latestState = _uiState.value
            _uiState.value = latestState.copy(
                visualDetailsByNorad = details?.let { latestState.visualDetailsByNorad + (noradId to it) }
                    ?: latestState.visualDetailsByNorad,
                loadingVisuals = latestState.loadingVisuals - noradId
            )
        }
    }

    private fun loadPage(page: Int, append: Boolean) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val cachedPage = getCachedSatelliteCatalogUseCase(
                query = currentState.query,
                page = page,
                pageSize = currentState.pageSize,
                category = currentState.selectedCategory
            )

            loadedSatellites = if (append) {
                (loadedSatellites + cachedPage.satellites).distinctBy { it.noradId }
            } else {
                cachedPage.satellites.distinctBy { it.noradId }
            }

            _uiState.value = currentState.copy(
                currentPage = cachedPage.page,
                totalItems = cachedPage.totalItems,
                hasNextPage = cachedPage.hasNextPage,
                isLoading = !append,
                isAppending = append,
                errorMessage = null,
                visualDetailsByNorad = if (append) currentState.visualDetailsByNorad else emptyMap(),
                loadingVisuals = emptySet()
            )
            applyFilters()

            runCatching {
                syncTleUseCase(
                    query = currentState.query,
                    page = page,
                    pageSize = currentState.pageSize,
                    category = currentState.selectedCategory
                )
            }.onSuccess { result ->
                loadedSatellites = if (append) {
                    (loadedSatellites + result.satellites).distinctBy { it.noradId }
                } else {
                    result.satellites.distinctBy { it.noradId }
                }

                _uiState.value = _uiState.value.copy(
                    currentPage = result.page,
                    totalItems = result.totalItems,
                    hasNextPage = result.hasNextPage,
                    isLoading = false,
                    isAppending = false,
                    errorMessage = null
                )
                applyFilters()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAppending = false,
                    errorMessage = if (loadedSatellites.isEmpty()) {
                        error.message ?: "No se pudo cargar el catalogo satelital"
                    } else {
                        null
                    }
                )
            }
        }
    }

    private fun applyFilters() {
        val state = _uiState.value
        _uiState.value = state.copy(
            satellites = filterSatellitesUseCase(
                satellites = loadedSatellites,
                query = state.query,
                selectedCategory = state.selectedCategory
            )
        )
    }

}
