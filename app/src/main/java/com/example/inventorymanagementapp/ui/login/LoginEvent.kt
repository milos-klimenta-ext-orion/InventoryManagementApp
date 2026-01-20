package com.example.inventorymanagementapp.ui.login

sealed class LoginEvent {
    object NavigateToDashboard: LoginEvent()
}