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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.elitec.satexplorer.feature.analitics.presentation.screens.DashBoardScreen
import com.elitec.satexplorer.feature.auth.domain.entity.AccountState
import com.elitec.satexplorer.feature.auth.domain.entity.SystemSettingsConfiguration
import com.elitec.satexplorer.feature.auth.domain.entity.User
import com.elitec.satexplorer.feature.auth.domain.entity.UserRank
import com.elitec.satexplorer.feature.auth.presentation.screens.ProfileScreen
import com.elitec.satexplorer.feature.satellite.presentation.screen.SatelliteScreen
import com.elitec.satexplorer.feature.tracking.domain.entity.SatelliteType
import com.elitec.satexplorer.feature.visualization.domain.entity.Globe
import com.elitec.satexplorer.feature.visualization.presentation.wrapper.GlobeScreen
import com.elitec.satexplorer.infrastructure.domain.DistanceUnitsMetrics
import com.elitec.satexplorer.infrastructure.domain.VelocityUnitsMetrics
import com.elitec.satexplorer.infrastructure.presentation.components.BottomNavBar
import com.elitec.satexplorer.infrastructure.presentation.model.NavBarItem
import com.elitec.satexplorer.infrastructure.presentation.navigation.utils.navigateTo
import com.elitec.satexplorer.feature.tracking.presentation.screens.ArTrackerScreen
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun InternalNavigationWrapper(
    modifier: Modifier = Modifier
) {
    // For test only
    val profileConfig = SystemSettingsConfiguration(
        refreshRate = 200f,
        isAutoStabilized = false,
        distanceUnitsMetrics = DistanceUnitsMetrics.KM,
        velocityUnitsMetrics = VelocityUnitsMetrics.KMHrs
    )
    val user = User(
        Random.nextLong(),
        "userTest",
        "test@mail.com",
        "23if2e",
        "",
        UserRank.OPERATOR,
        AccountState.ACTIVE,
        profileConfig)
    val backStack = rememberNavBackStack(InternalRoutes.MainHome)
    val navItems = listOf(
        NavBarItem("Home", Icons.Default.Dashboard, action = {
            backStack.navigateTo(InternalRoutes.MainHome)
        }),
        NavBarItem("Orbit", Icons.Default.RadioButtonUnchecked, action = {
            backStack.navigateTo(InternalRoutes.Orbit)
        }),
        NavBarItem("Search", Icons.Default.Search, action = {
            backStack.navigateTo(InternalRoutes.Search)
        }),
        NavBarItem("Sky", Icons.Default.RemoveRedEye, action = {
            backStack.navigateTo(InternalRoutes.ARView)
        }),
        NavBarItem("Profile", Icons.Default.AccountCircle, action = {
            backStack.navigateTo(InternalRoutes.Profile)
        }),
    )
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        NavDisplay(
            backStack = backStack,
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(250)
                )
            },
            popTransitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(250)
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(250)
                )
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
                    GlobeScreen(modifier = Modifier.fillMaxSize())
                }
                entry<InternalRoutes.MainHome> {
                    DashBoardScreen(
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
                        modifier = Modifier.fillMaxSize()
                    )
                }
                entry<InternalRoutes.Profile> {
                    ProfileScreen(
                        user = user,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        )
        BottomNavBar(
            navItems = navItems,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
        )
    }
}
