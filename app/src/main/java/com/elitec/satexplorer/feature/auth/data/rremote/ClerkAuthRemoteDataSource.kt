package com.elitec.satexplorer.feature.auth.data.rremote

import com.elitec.satexplorer.feature.auth.data.dto.ClerkAuthResponseDto
import com.elitec.satexplorer.feature.auth.data.dto.ClerkChangePasswordRequestDto
import com.elitec.satexplorer.feature.auth.data.dto.ClerkLoginRequestDto
import com.elitec.satexplorer.feature.auth.data.dto.ClerkRegisterRequestDto
import kotlinx.coroutines.delay
import kotlin.random.Random

class ClerkAuthRemoteDataSource {

    suspend fun login(request: ClerkLoginRequestDto): Result<ClerkAuthResponseDto> {
        delay(300)
        if (request.email.isBlank()) return Result.failure(IllegalArgumentException("Email is required"))
        if (request.password.isBlank() && request.provider == "password") {
            return Result.failure(IllegalArgumentException("Password is required"))
        }
        return Result.success(
            ClerkAuthResponseDto(
                userId = "usr_${Random.nextInt(1000, 9999)}",
                username = request.email.substringBefore("@").ifBlank { "operator" },
                email = request.email,
                sessionId = "sess_${Random.nextInt(10000, 99999)}",
                provider = request.provider
            )
        )
    }

    suspend fun register(request: ClerkRegisterRequestDto): Result<ClerkAuthResponseDto> {
        delay(350)
        if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank()) {
            return Result.failure(IllegalArgumentException("Username, email and password are required"))
        }
        return Result.success(
            ClerkAuthResponseDto(
                userId = "usr_${Random.nextInt(1000, 9999)}",
                username = request.username,
                email = request.email,
                sessionId = "sess_${Random.nextInt(10000, 99999)}",
                provider = "password"
            )
        )
    }

    suspend fun changePassword(request: ClerkChangePasswordRequestDto): Result<Unit> {
        delay(200)
        if (request.newPassword.length < 8) {
            return Result.failure(IllegalArgumentException("New password must contain at least 8 characters"))
        }
        return Result.success(Unit)
    }
}