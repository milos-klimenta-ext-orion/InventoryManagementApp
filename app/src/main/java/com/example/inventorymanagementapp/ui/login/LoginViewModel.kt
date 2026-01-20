package com.example.inventorymanagementapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginPreferences: LoginPreferences
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events

    init {
        // Load persisted login state
        viewModelScope.launch {
            loginPreferences.isLoggedInFlow.collect { loggedIn ->
                if (loggedIn) {
                    _uiState.update { it.copy(isLoggedIn = true) }
                    _events.emit(LoginEvent.NavigateToDashboard)
                }
            }
        }
    }

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(userName = username, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun login() {
        val state = _uiState.value

        if (state.userName.isBlank() || state.password.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Username and password must not be empty")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Simulate network delay
            delay(1000)

            if (state.userName == "admin" && state.password == "admin") {
                loginPreferences.setLoggedIn(true)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoggedIn = true
                    )
                }

                _events.emit(LoginEvent.NavigateToDashboard)
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Invalid credentials"
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            loginPreferences.setLoggedIn(false)
            _uiState.update { it.copy(isLoggedIn = false, userName = "", password = "") }
        }
    }
}