package com.elitec.satexplorer.infrastructure.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface MainRoutes: NavKey {
    @Serializable
    object Splash: MainRoutes

    @Serializable
    object Landing: MainRoutes
    @Serializable
    object Login: MainRoutes

    @Serializable
    data class Home( val userId: String ): MainRoutes

    @Serializable
    object Register: MainRoutes

    @Serializable
    object Error: MainRoutes

    // From Test only
    @Serializable
    data class Details(val id: String): MainRoutes
}