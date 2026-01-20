package com.example.inventorymanagementapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.inventorymanagementapp.navigation.bottomnavigation.InventoryBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            InventoryBottomBar(navController)
        }
    ) { padding ->
        InventoryNavGraph(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}