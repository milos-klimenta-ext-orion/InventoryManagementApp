package com.example.inventorymanagementapp.ui.login

import com.example.inventorymanagementapp.MainDispatcherRule
import com.example.inventorymanagementapp.fakes.FakeLoginPreferences
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var preferences: FakeLoginPreferences
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        preferences = FakeLoginPreferences()
        viewModel = LoginViewModel(preferences)
    }

    @Test
    fun initialStateIsEmptyAndNotLoggedIn() = runTest {
        val state = viewModel.uiState.value

        assertFalse(state.isLoggedIn)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals("", state.userName)
        assertEquals("", state.password)
    }

    @Test
    fun onUsernameChangeUpdatesUsernameAndClearsError() = runTest {
        viewModel.onUsernameChange("admin")

        val state = viewModel.uiState.value
        assertEquals("admin", state.userName)
        assertNull(state.errorMessage)
    }

    @Test
    fun onPasswordChangeUpdatesPasswordAndClearsError() = runTest {
        viewModel.onPasswordChange("admin")

        val state = viewModel.uiState.value
        assertEquals("admin", state.password)
        assertNull(state.errorMessage)
    }

    @Test
    fun loginWithEmptyCredentialsShowsError() = runTest {
        viewModel.login()

        val state = viewModel.uiState.value
        assertEquals("Username and password must not be empty", state.errorMessage)
        assertFalse(state.isLoading)
    }

    @Test
    fun loginWithInvalidCredentialsShowsError() = runTest {
        viewModel.onUsernameChange("user")
        viewModel.onPasswordChange("wrong")

        viewModel.login()

        advanceTimeBy(1_000)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Invalid credentials", state.errorMessage)
        assertFalse(state.isLoading)
        assertFalse(state.isLoggedIn)
    }

    @Test
    fun loginWithCorrectCredentialsLogsIn() = runTest {
        viewModel.onUsernameChange("admin")
        viewModel.onPasswordChange("admin")

        viewModel.login()

        advanceTimeBy(1_000)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isLoggedIn)
        assertFalse(state.isLoading)
    }

    @Test
    fun successfulLoginEmitsNavigationEvent() = runTest {
        viewModel.onUsernameChange("admin")
        viewModel.onPasswordChange("admin")

        val event = async {
            viewModel.events.first()
        }

        viewModel.login()
        advanceTimeBy(1_000)
        advanceUntilIdle()

        assertEquals(LoginEvent.NavigateToDashboard, event.await())
    }

    @Test
    fun alreadyLoggedInEmitsNavigationEventOnInit() = runTest {
        preferences.setLoggedIn(true)

        viewModel = LoginViewModel(preferences)

        val event = viewModel.events.first()

        assertEquals(LoginEvent.NavigateToDashboard, event)
    }

    @Test
    fun logoutClearsLoginState() = runTest {
        preferences.setLoggedIn(true)
        viewModel = LoginViewModel(preferences)

        viewModel.logout()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoggedIn)
        assertEquals("", state.userName)
        assertEquals("", state.password)
    }

}