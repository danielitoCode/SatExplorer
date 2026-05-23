package com.elitec.satexplorer.feature.auth.presentation.model

import com.elitec.satexplorer.feature.auth.domain.entity.AuthSession

data class AuthUiState(
    val authInProgress: Boolean = false,
    val registerInProgress: Boolean = false,
    val passwordChangeInProgress: Boolean = false,
    val session: AuthSession? = null,
    val error: String? = null,
    val passwordChanged: Boolean = false
)