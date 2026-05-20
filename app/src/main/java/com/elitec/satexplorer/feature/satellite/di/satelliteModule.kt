package com.elitec.satexplorer.feature.satellite.di

import androidx.room.Room
import com.elitec.satexplorer.feature.satellite.data.mapper.SatelliteCatalogMapper
import com.elitec.satexplorer.feature.satellite.data.remote.SatNogsRemoteDataSource
import com.elitec.satexplorer.feature.satellite.data.remote.TleCatalogRemoteDataSource
import com.elitec.satexplorer.feature.satellite.data.repository.SatelliteCatalogRepositoryImpl
import com.elitec.satexplorer.feature.satellite.domain.caseuse.FilterSatellitesUseCase
import com.elitec.satexplorer.feature.satellite.domain.caseuse.GetCachedSatelliteCatalogUseCase
import com.elitec.satexplorer.feature.satellite.domain.caseuse.LoadSatelliteVisualDetailsUseCase
import com.elitec.satexplorer.feature.satellite.domain.caseuse.SyncTleUseCase
import com.elitec.satexplorer.feature.satellite.domain.repository.SatelliteCatalogRepository
import com.elitec.satexplorer.feature.satellite.presentation.viewmodel.SatelliteSearchViewModel
import com.elitec.satexplorer.infrastructure.data.local.SatExplorerDatabase
import com.elitec.satexplorer.infrastructure.data.local.SatelliteCacheLocalDataSource
import com.elitec.satexplorer.infrastructure.data.local.mapper.SatelliteCacheMapper
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val satelliteModule = module {
    single {
        Room.databaseBuilder(
            context = get(),
            klass = SatExplorerDatabase::class.java,
            name = "sat_explorer.db"
        ).fallbackToDestructiveMigration().build()
    }
    single { get<SatExplorerDatabase>().satelliteCacheDao() }
    single { SatelliteCacheMapper() }
    single { SatelliteCacheLocalDataSource(get(), get()) }
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single { SatelliteCatalogMapper() }
    single { TleCatalogRemoteDataSource(get(), get()) }
    single { SatNogsRemoteDataSource(get(), get()) }
    single<SatelliteCatalogRepository> { SatelliteCatalogRepositoryImpl(get(), get(), get(), get()) }
    factory { GetCachedSatelliteCatalogUseCase(get()) }
    factory { SyncTleUseCase(get()) }
    factory { FilterSatellitesUseCase() }
    factory { LoadSatelliteVisualDetailsUseCase(get()) }
    viewModel { SatelliteSearchViewModel(get(), get(), get(), get()) }
}
