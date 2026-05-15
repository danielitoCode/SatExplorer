package com.elitec.satexplorer.infrastructure.presentation.navigation

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import com.elitec.satexplorer.infrastructure.presentation.navigation.utils.AdaptiveLayoutSpec


@Composable
fun rememberAdaptiveLayoutSpec(): AdaptiveLayoutSpec {
    val configuration = LocalConfiguration.current
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    return remember(configuration, windowSizeClass) {
        resolveAdaptiveLayoutSpec(configuration, windowSizeClass)
    }
}