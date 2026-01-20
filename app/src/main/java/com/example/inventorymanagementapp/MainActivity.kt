package com.example.inventorymanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventorymanagementapp.navigation.MainScaffold
import com.example.inventorymanagementapp.navigation.Screen
import com.example.inventorymanagementapp.ui.login.LoginScreen
import com.example.inventorymanagementapp.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val loginViewModel: LoginViewModel = hiltViewModel()
            val isLoggedIn by loginViewModel.uiState.collectAsState()

            NavHost(
                navController = navController,
                startDestination = if (isLoggedIn.isLoggedIn) "main" else Screen.Login.route
            ) {

                composable(Screen.Login.route) {
                    LoginScreen(
                        onLoginSuccess = {
                            navController.navigate("main") {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    )
                }

                composable("main") {
                    MainScaffold()
                }
            }
        }
    }
}