package com.elitec.satexplorer.infrastructure.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector

data class NavBarItem(
    val tittle: String,
    val icon: ImageVector,
    val action: () -> Unit
)