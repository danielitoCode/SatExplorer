package com.elitec.satexplorer.feature.auth.domain.caseuse

import com.elitec.satexplorer.feature.auth.domain.repository.AuthRepository

class ChangePasswordWithClerkUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(userId: String, currentPassword: String, newPassword: String): Result<Unit> {
        return repository.changePassword(userId, currentPassword, newPassword)
    }
}