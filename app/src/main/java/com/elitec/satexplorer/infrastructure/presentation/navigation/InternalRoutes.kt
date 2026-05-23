package com.elitec.satexplorer.infrastructure.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface InternalRoutes: NavKey {
    @Serializable
    object MainHome : InternalRoutes

    @Serializable
    object Profile : InternalRoutes

    @Serializable
    object Orbit: InternalRoutes

    @Serializable
    object ARView: InternalRoutes

    @Serializable
    object Search: InternalRoutes

    @Serializable
    object Settings: InternalRoutes
}