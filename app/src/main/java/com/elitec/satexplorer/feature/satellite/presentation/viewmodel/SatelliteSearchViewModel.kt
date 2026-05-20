package com.elitec.satexplorer.feature.satellite.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elitec.satexplorer.feature.satellite.domain.caseuse.FilterSatellitesUseCase
import com.elitec.satexplorer.feature.satellite.domain.caseuse.SyncTleUseCase
import com.elitec.satexplorer.feature.satellite.presentation.model.SatelliteSearchUiState
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import com.elitec.satexplorer.feature.tracking.domain.entity.SatelliteType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SatelliteSearchViewModel(
    private val syncTleUseCase: SyncTleUseCase,
    private val filterSatellitesUseCase: FilterSatellitesUseCase
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

    fun onTypeSelected(type: SatelliteType?) {
        _uiState.value = _uiState.value.copy(
            selectedType = if (_uiState.value.selectedType == type) null else type
        )
        applyFilters()
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

    private fun loadPage(page: Int, append: Boolean) {
        viewModelScope.launch {
            val currentState = _uiState.value
            _uiState.value = currentState.copy(
                isLoading = !append,
                isAppending = append,
                errorMessage = null
            )

            runCatching {
                syncTleUseCase(
                    query = currentState.query,
                    page = page,
                    pageSize = currentState.pageSize
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
                    errorMessage = error.message ?: "No se pudo cargar el catalogo satelital"
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
                selectedType = state.selectedType
            )
        )
    }
}
