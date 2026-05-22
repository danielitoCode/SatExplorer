package com.elitec.satexplorer.feature.alerts.di

import com.elitec.satexplorer.feature.alerts.data.notifications.WorkManagerAlertsNotifier
import com.elitec.satexplorer.feature.alerts.domain.caseuse.GenerateAlertsUseCase
import com.elitec.satexplorer.feature.alerts.domain.caseuse.ScheduleNotificationUseCase
import com.elitec.satexplorer.feature.alerts.domain.repository.AlertsNotifier
import com.elitec.satexplorer.feature.analitics.domain.caseuse.LogEventUseCase
import com.elitec.satexplorer.feature.analitics.domain.caseuse.TrackPerformanceUseCase
import com.elitec.satexplorer.feature.analitics.presentation.viewmodel.DashboardViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val analyticsModule = module {
    factory { TrackPerformanceUseCase() }
    single { LogEventUseCase() }
    factory { GenerateAlertsUseCase() }
    single<AlertsNotifier> { WorkManagerAlertsNotifier(androidContext()) }
    factory { ScheduleNotificationUseCase(get()) }
    viewModel { DashboardViewModel(get(), get(), get(), get()) }
}