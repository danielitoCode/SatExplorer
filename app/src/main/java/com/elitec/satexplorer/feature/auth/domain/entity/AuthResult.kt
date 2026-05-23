package com.elitec.satexplorer.feature.auth.domain.entity

sealed class AuthResult {
    data class Success(val session: AuthSession): AuthResult()
    data class Error(val message: String): AuthResult()
}