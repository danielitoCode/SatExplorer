package com.elitec.satexplorer.feature.auth.domain.caseuse

import com.elitec.satexplorer.feature.auth.domain.entity.AuthResult
import com.elitec.satexplorer.feature.auth.domain.repository.AuthRepository

class LoginWithClerkUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, provider: String): AuthResult {
        return repository.login(email, password, provider)
    }
}