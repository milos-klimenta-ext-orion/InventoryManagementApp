package com.example.inventorymanagementapp.ui.login

import kotlinx.coroutines.flow.Flow


interface LoginPreferences {
    val isLoggedInFlow: Flow<Boolean>
    suspend fun setLoggedIn(loggedIn: Boolean)
}
