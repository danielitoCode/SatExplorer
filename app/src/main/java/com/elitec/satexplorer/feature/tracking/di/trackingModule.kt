package com.elitec.satexplorer.feature.tracking.di

import com.elitec.satexplorer.feature.tracking.data.mapper.TleMapper
import com.elitec.satexplorer.feature.tracking.data.remote.CelestrakRemoteDataSource
import com.elitec.satexplorer.feature.tracking.data.repository.SatelliteRepositoryImpl
import com.elitec.satexplorer.feature.tracking.domain.caseuse.ComputeSatellitePositionUseCase
import com.elitec.satexplorer.feature.tracking.domain.caseuse.LoadSatelliteFromApiUseCase
import com.elitec.satexplorer.feature.tracking.domain.caseuse.LoadSatelliteManualUseCase
import com.elitec.satexplorer.feature.tracking.domain.repository.SatelliteRepository
import com.elitec.satexplorer.feature.tracking.presentation.viewmodel.SatelliteInputViewModel
import android.content.Context
import android.hardware.SensorManager
import com.elitec.satexplorer.feature.tracking.presentation.viewmodel.ArTrackerViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val trackingModule = module {
    single { HttpClient(OkHttp) }
    single { TleMapper() }
    single { CelestrakRemoteDataSource(get()) }
    single<SatelliteRepository> { SatelliteRepositoryImpl(get(), get()) }
    factory { LoadSatelliteFromApiUseCase(get()) }
    factory { LoadSatelliteManualUseCase(get()) }
    factory { ComputeSatellitePositionUseCase() }
    viewModel { SatelliteInputViewModel(get(), get(), get(), get()) }
    
    // Inyección de SensorManager del sistema y ArTrackerViewModel
    single { androidContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    viewModel { ArTrackerViewModel(get()) }
}