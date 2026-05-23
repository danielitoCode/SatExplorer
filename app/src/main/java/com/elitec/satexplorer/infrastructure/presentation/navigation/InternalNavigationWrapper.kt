package com.elitec.satexplorer.infrastructure.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elitec.satexplorer.feature.analitics.presentation.screens.DashBoardScreen
import com.elitec.satexplorer.feature.analitics.presentation.viewmodel.DashboardViewModel
import com.elitec.satexplorer.feature.auth.domain.entity.AccountState
import com.elitec.satexplorer.feature.auth.domain.entity.SystemSettingsConfiguration
import com.elitec.satexplorer.feature.auth.domain.entity.User
import com.elitec.satexplorer.feature.auth.domain.entity.UserRank
import com.elitec.satexplorer.feature.auth.presentation.screens.ProfileScreen
import com.elitec.satexplorer.feature.auth.presentation.screens.SettingsScreen
import com.elitec.satexplorer.feature.satellite.presentation.screen.SatelliteScreen
import com.elitec.satexplorer.feature.visualization.domain.entity.Globe
import com.elitec.satexplorer.feature.tracking.presentation.viewmodel.SatelliteInputViewModel
import com.elitec.satexplorer.feature.visualization.presentation.wrapper.GlobeScreen
import com.elitec.satexplorer.infrastructure.domain.DistanceUnitsMetrics
import com.elitec.satexplorer.infrastructure.domain.VelocityUnitsMetrics
import com.elitec.satexplorer.infrastructure.presentation.components.BottomNavBar
import com.elitec.satexplorer.infrastructure.presentation.model.NavBarItem
import com.elitec.satexplorer.infrastructure.presentation.navigation.utils.navigateTo
import com.elitec.satexplorer.feature.tracking.presentation.screens.ArTrackerScreen
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun InternalNavigationWrapper(
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profileConfig = SystemSettingsConfiguration(200f, false, DistanceUnitsMetrics.KM, VelocityUnitsMetrics.KMHrs)
    val user = User(Random.nextLong(), "userTest", "test@mail.com", "23if2e", "", UserRank.OPERATOR, AccountState.ACTIVE, profileConfig)

    val backStack = rememberNavBackStack(InternalRoutes.MainHome)
    val trackingViewModel: SatelliteInputViewModel = koinViewModel()
    val trackingState by trackingViewModel.uiState.collectAsStateWithLifecycle()

    val dashboardViewModel: DashboardViewModel = koinViewModel()
    LaunchedEffect(trackingState) {
        dashboardViewModel.syncWithTrackingState(trackingState)
    }
    val dashboardState by dashboardViewModel.uiState.collectAsStateWithLifecycle()

    val navItems = listOf(
        NavBarItem("Home", Icons.Default.Dashboard) { backStack.navigateTo(InternalRoutes.MainHome) },
        NavBarItem("Orbit", Icons.Default.RadioButtonUnchecked) { backStack.navigateTo(InternalRoutes.Orbit) },
        NavBarItem("Search", Icons.Default.Search) { backStack.navigateTo(InternalRoutes.Search) },
        NavBarItem("Sky", Icons.Default.RemoveRedEye) { backStack.navigateTo(InternalRoutes.ARView) },
        NavBarItem("Profile", Icons.Default.AccountCircle) { backStack.navigateTo(InternalRoutes.Profile)}
    )
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        NavDisplay(
            backStack = backStack,
            transitionSpec = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(250)) togetherWith
                        slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(250))
            },
            popTransitionSpec = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(250)) togetherWith
                        slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(250))
            },
            predictivePopTransitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(250)
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(250)
                )
            },
            modifier = Modifier.weight(1f).fillMaxSize().padding(10.dp),
            entryProvider = entryProvider {
                entry<InternalRoutes.Orbit> {
                    GlobeScreen(
                        selectedSatellite = trackingState.satellite,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                entry<InternalRoutes.MainHome> {
                    DashBoardScreen(
                        uiState = dashboardState,
                        onNotifyOnPass = dashboardViewModel::notifyOnPass,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                entry<InternalRoutes.ARView> {
                    ArTrackerScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                entry<InternalRoutes.Search> {
                    SatelliteScreen(
                        modifier = Modifier.fillMaxSize(),
                        onSatelliteSelected = { satellite ->
                            trackingViewModel.loadFromApi(satellite.noradId)
                            backStack.navigateTo(InternalRoutes.Orbit)
                        }
                    )
                }
                entry<InternalRoutes.Profile> {
                    ProfileScreen(
                        user = user,
                        onOpenSettings = { backStack.navigateTo(InternalRoutes.Settings) },
                        onSignOut = onSignOut,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                entry<InternalRoutes.Settings> {
                    SettingsScreen(
                        onBack = { backStack.removeLastOrNull() },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        )
        val currentRoute = backStack.lastOrNull()
        val selectedItemName = when (backStack.lastOrNull()) {
            InternalRoutes.MainHome -> "Home"
            InternalRoutes.Orbit -> "Orbit"
            InternalRoutes.Search -> "Search"
            InternalRoutes.ARView -> "Sky"
            InternalRoutes.Profile -> "Profile"
            else -> "Home"
        }

        BottomNavBar(
            navItems = navItems,
            selectedItemName = selectedItemName,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
        )
    }
}
