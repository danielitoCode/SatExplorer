package com.elitec.satexplorer.feature.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elitec.satexplorer.feature.auth.domain.caseuse.ChangePasswordWithClerkUseCase
import com.elitec.satexplorer.feature.auth.domain.caseuse.LoginWithClerkUseCase
import com.elitec.satexplorer.feature.auth.domain.caseuse.RegisterWithClerkUseCase
import com.elitec.satexplorer.feature.auth.domain.entity.AuthResult
import com.elitec.satexplorer.feature.auth.presentation.model.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginWithClerkUseCase: LoginWithClerkUseCase,
    private val registerWithClerkUseCase: RegisterWithClerkUseCase,
    private val changePasswordWithClerkUseCase: ChangePasswordWithClerkUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String, provider: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(authInProgress = true, error = null) }
            when (val result = loginWithClerkUseCase(email, password, provider)) {
                is AuthResult.Success -> _uiState.update { state ->
                    state.copy(authInProgress = false, session = result.session, error = null)
                }
                is AuthResult.Error -> _uiState.update { state ->
                    state.copy(authInProgress = false, error = result.message)
                }
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(registerInProgress = true, error = null) }
            when (val result = registerWithClerkUseCase(username, email, password)) {
                is AuthResult.Success -> _uiState.update { state ->
                    state.copy(registerInProgress = false, session = result.session, error = null)
                }
                is AuthResult.Error -> _uiState.update { state ->
                    state.copy(registerInProgress = false, error = result.message)
                }
            }
        }
    }

    fun changePassword(userId: String, currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(passwordChangeInProgress = true, error = null, passwordChanged = false) }
            val result = changePasswordWithClerkUseCase(userId, currentPassword, newPassword)
            _uiState.update { state ->
                state.copy(
                    passwordChangeInProgress = false,
                    passwordChanged = result.isSuccess,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}