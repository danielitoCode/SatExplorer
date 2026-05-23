package com.elitec.satexplorer.feature.auth.domain.caseuse

import com.elitec.satexplorer.feature.auth.domain.entity.AuthResult
import com.elitec.satexplorer.feature.auth.domain.repository.AuthRepository

class RegisterWithClerkUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, email: String, password: String): AuthResult {
        return repository.register(username, email, password)
    }
}