package com.example.inventorymanagementapp.fakes

import com.example.inventorymanagementapp.ui.login.LoginPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLoginPreferences: LoginPreferences {

    private val _isLoggedInFlow = MutableStateFlow(false)
    override val isLoggedInFlow: Flow<Boolean> = _isLoggedInFlow

    override suspend fun setLoggedIn(value: Boolean) {
        _isLoggedInFlow.value = value
    }
}