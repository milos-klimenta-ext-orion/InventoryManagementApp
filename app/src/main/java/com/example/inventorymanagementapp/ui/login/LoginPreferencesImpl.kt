package com.example.inventorymanagementapp.ui.login

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class LoginPreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LoginPreferences {

    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

    override val isLoggedInFlow: Flow<Boolean> =
        context.dataStore.data
            .map { prefs -> prefs[IS_LOGGED_IN] ?: false }

    override suspend fun setLoggedIn(loggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = loggedIn
        }
    }
}
