package com.elitec.satexplorer.feature.auth.domain.repository

import com.elitec.satexplorer.feature.auth.domain.entity.AuthResult

interface AuthRepository {
    suspend fun login(email: String, password: String, provider: String): AuthResult
    suspend fun register(username: String, email: String, password: String): AuthResult
    suspend fun changePassword(userId: String, currentPassword: String, newPassword: String): Result<Unit>
}