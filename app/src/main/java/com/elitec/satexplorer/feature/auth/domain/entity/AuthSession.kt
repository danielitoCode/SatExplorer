package com.elitec.satexplorer.feature.auth.domain.entity

data class AuthSession(
    val userId: String,
    val username: String,
    val email: String,
    val sessionId: String,
    val provider: String
)