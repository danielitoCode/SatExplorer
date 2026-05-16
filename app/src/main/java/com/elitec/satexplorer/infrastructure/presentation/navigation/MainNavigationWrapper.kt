package com.elitec.satexplorer.infrastructure.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.elitec.satexplorer.feature.auth.presentation.screens.LoginScreen
import com.elitec.satexplorer.feature.auth.presentation.screens.SplashScreen
import com.elitec.satexplorer.infrastructure.presentation.navigation.utils.navigateBack
import com.elitec.satexplorer.infrastructure.presentation.navigation.utils.navigateTo
import java.util.Map.entry

@Composable
fun MainNavigationWrapper(
    contentPaddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(MainRoutes.Splash)

    fun resetRoot(destination: MainRoutes) {
        while (backStack.isNotEmpty()) {
            backStack.removeLastOrNull()
        }
        backStack.navigateTo(destination)
    }

    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = modifier.fillMaxSize()
    ) {

        NavDisplay(
            modifier = Modifier.fillMaxSize(),
            backStack = backStack,
            onBack = { backStack.navigateBack() },
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
            entryProvider = entryProvider {
                entry<MainRoutes.Splash> {
                    SplashScreen(
                        navigateTo = { route ->
                            backStack.navigateTo(route)
                        },
                        modifier = Modifier.fillMaxSize().padding(
                            top = contentPaddingValues.calculateTopPadding()
                        )
                    )
                }
                entry<MainRoutes.Login> {
                    LoginScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        )
    }
}