package com.elitec.satexplorer.feature.auth.di

import com.elitec.satexplorer.feature.auth.data.repository.AuthRepositoryImpl
import com.elitec.satexplorer.feature.auth.data.rremote.ClerkAuthRemoteDataSource
import com.elitec.satexplorer.feature.auth.domain.caseuse.ChangePasswordWithClerkUseCase
import com.elitec.satexplorer.feature.auth.domain.caseuse.LoginWithClerkUseCase
import com.elitec.satexplorer.feature.auth.domain.caseuse.RegisterWithClerkUseCase
import com.elitec.satexplorer.feature.auth.domain.repository.AuthRepository
import com.elitec.satexplorer.feature.auth.presentation.viewmodel.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val authModule = module {
    single { ClerkAuthRemoteDataSource() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    factory { LoginWithClerkUseCase(get()) }
    factory { RegisterWithClerkUseCase(get()) }
    factory { ChangePasswordWithClerkUseCase(get()) }

    viewModel { AuthViewModel(get(), get(), get()) }
}