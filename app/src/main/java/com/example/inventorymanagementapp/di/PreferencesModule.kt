package com.example.inventorymanagementapp.di

import com.example.inventorymanagementapp.ui.login.LoginPreferences
import com.example.inventorymanagementapp.ui.login.LoginPreferencesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Binds
    abstract fun bindLoginPreferences(
        impl: LoginPreferencesImpl
    ): LoginPreferences
}