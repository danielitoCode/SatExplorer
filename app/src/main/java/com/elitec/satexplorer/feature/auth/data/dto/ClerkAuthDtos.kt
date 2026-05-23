package com.elitec.satexplorer.feature.auth.data.dto

data class ClerkLoginRequestDto(
    val email: String,
    val password: String,
    val provider: String
)

data class ClerkRegisterRequestDto(
    val username: String,
    val email: String,
    val password: String
)

data class ClerkChangePasswordRequestDto(
    val userId: String,
    val currentPassword: String,
    val newPassword: String
)

data class ClerkAuthResponseDto(
    val userId: String,
    val username: String,
    val email: String,
    val sessionId: String,
    val provider: String
)

