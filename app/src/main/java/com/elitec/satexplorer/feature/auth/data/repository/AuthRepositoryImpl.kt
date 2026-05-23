package com.elitec.satexplorer.feature.auth.data.repository

import com.elitec.satexplorer.feature.auth.data.dto.ClerkAuthMapper
import com.elitec.satexplorer.feature.auth.data.dto.ClerkChangePasswordRequestDto
import com.elitec.satexplorer.feature.auth.data.dto.ClerkLoginRequestDto
import com.elitec.satexplorer.feature.auth.data.dto.ClerkRegisterRequestDto
import com.elitec.satexplorer.feature.auth.data.rremote.ClerkAuthRemoteDataSource
import com.elitec.satexplorer.feature.auth.domain.entity.AuthResult
import com.elitec.satexplorer.feature.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remoteDataSource: ClerkAuthRemoteDataSource
): AuthRepository {

    override suspend fun login(email: String, password: String, provider: String): AuthResult {
        val result = remoteDataSource.login(
            ClerkLoginRequestDto(email = email, password = password, provider = provider)
        )

        return result.fold(
            onSuccess = { AuthResult.Success(ClerkAuthMapper.toDomain(it)) },
            onFailure = { AuthResult.Error(it.message ?: "Login failed") }
        )
    }

    override suspend fun register(username: String, email: String, password: String): AuthResult {
        val result = remoteDataSource.register(
            ClerkRegisterRequestDto(username = username, email = email, password = password)
        )

        return result.fold(
            onSuccess = { AuthResult.Success(ClerkAuthMapper.toDomain(it)) },
            onFailure = { AuthResult.Error(it.message ?: "Register failed") }
        )
    }

    override suspend fun changePassword(userId: String, currentPassword: String, newPassword: String): Result<Unit> {
        return remoteDataSource.changePassword(
            ClerkChangePasswordRequestDto(
                userId = userId,
                currentPassword = currentPassword,
                newPassword = newPassword
            )
        )
    }
}