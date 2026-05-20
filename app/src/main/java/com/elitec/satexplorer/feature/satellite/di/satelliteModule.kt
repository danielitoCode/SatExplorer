package com.elitec.satexplorer.feature.satellite.di

import com.elitec.satexplorer.feature.satellite.data.mapper.SatelliteCatalogMapper
import com.elitec.satexplorer.feature.satellite.data.remote.TleCatalogRemoteDataSource
import com.elitec.satexplorer.feature.satellite.data.repository.SatelliteCatalogRepositoryImpl
import com.elitec.satexplorer.feature.satellite.domain.caseuse.FilterSatellitesUseCase
import com.elitec.satexplorer.feature.satellite.domain.caseuse.SyncTleUseCase
import com.elitec.satexplorer.feature.satellite.domain.repository.SatelliteCatalogRepository
import com.elitec.satexplorer.feature.satellite.presentation.viewmodel.SatelliteSearchViewModel
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val satelliteModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single { SatelliteCatalogMapper() }
    single { TleCatalogRemoteDataSource(get(), get()) }
    single<SatelliteCatalogRepository> { SatelliteCatalogRepositoryImpl(get(), get()) }
    factory { SyncTleUseCase(get()) }
    factory { FilterSatellitesUseCase() }
    viewModel { SatelliteSearchViewModel(get(), get()) }
}
