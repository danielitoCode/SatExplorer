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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.elitec.satexplorer.feature.auth.presentation.model.AuthenticationProvider
import com.elitec.satexplorer.feature.auth.presentation.screens.LoginScreen
import com.elitec.satexplorer.feature.auth.presentation.screens.RegistrationScreen
import com.elitec.satexplorer.feature.auth.presentation.screens.SplashScreen
import com.elitec.satexplorer.feature.auth.presentation.viewmodel.AuthViewModel
import com.elitec.satexplorer.infrastructure.presentation.navigation.utils.navigateBack
import com.elitec.satexplorer.infrastructure.presentation.navigation.utils.navigateTo
import com.elitec.satexplorer.infrastructure.presentation.screens.OnBoardScreen
import org.koin.androidx.compose.koinViewModel
import java.util.Map.entry

@Composable
fun MainNavigationWrapper(
    contentPaddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(MainRoutes.Splash)

    val authViewModel: AuthViewModel = koinViewModel()
    val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()

    fun resetRoot(destination: MainRoutes) {
        while (backStack.isNotEmpty()) {
            backStack.removeLastOrNull()
        }
        backStack.navigateTo(destination)
    }

    fun resolveSessionDisplayName(username: String, email: String): String {
        val normalizedUser = username.trim()
        if (normalizedUser.isNotEmpty()) return normalizedUser

        val normalizedEmail = email.trim()
        if (normalizedEmail.isEmpty()) return "operator"
        return normalizedEmail.substringBefore("@").ifBlank { normalizedEmail }
    }

    LaunchedEffect(authUiState.session?.sessionId) {
        val session = authUiState.session ?: return@LaunchedEffect
        resetRoot(MainRoutes.Home(session.username.ifBlank { session.email }))
    }

    LaunchedEffect(authUiState.session?.sessionId) {
        val session = authUiState.session ?: return@LaunchedEffect
        resetRoot(
            MainRoutes.Home(
                resolveSessionDisplayName(
                    username = session.username,
                    email = session.email
                )
            )
        )
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
                entry<MainRoutes.Landing> {
                    OnBoardScreen(
                        navigateTo = { route -> backStack.navigateTo(route) },
                        modifier = Modifier.fillMaxSize().padding(top = contentPaddingValues.calculateTopPadding())
                    )
                }
                entry<MainRoutes.Register> {
                    RegistrationScreen(
                        navigateTo = { route -> backStack.navigateTo(route) },
                        onRegisterWithClerk = { userName, email, password ->
                            authViewModel.register(userName, email, password)
                        },
                        registerInProgress = authUiState.registerInProgress,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                entry<MainRoutes.Login> {
                    LoginScreen(
                        navigateTo = { route -> backStack.navigateTo(route) },
                        onAuthenticate = { provider, email, password ->
                            val authProvider = when (provider) {
                                AuthenticationProvider.ClerkPassword -> "password"
                                AuthenticationProvider.ClerkGoogle -> "oauth_google"
                                AuthenticationProvider.ClerkGithub -> "oauth_github"
                            }
                            authViewModel.login(email, password, authProvider)
                        },
                        authInProgress = authUiState.authInProgress,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                entry<MainRoutes.Home> { key ->
                    InternalNavigationWrapper(
                        onSignOut = { resetRoot(MainRoutes.Login) },
                        modifier = Modifier.fillMaxSize().padding(
                            top = contentPaddingValues.calculateTopPadding(),
                            end = 10.dp,
                            start = 10.dp,
                            bottom = contentPaddingValues.calculateBottomPadding() + 5.dp
                        )
                    )
                }
            }
        )
    }
}

