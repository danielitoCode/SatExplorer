package com.elitec.satexplorer.infrastructure.presentation.navigation.utils

import androidx.compose.runtime.Immutable

@Immutable
data class AdaptiveLayoutSpec(
    val posture: DevicePosture,
    val showListAndDetail: Boolean,
    val showTopBarInDetail: Boolean,
    val maxContentWidthDp: Int,
)