package com.example.inventorymanagementapp.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inventorymanagementapp.ui.dashboard.DashboardScreen
import com.example.inventorymanagementapp.ui.login.LoginScreen
import com.example.inventorymanagementapp.ui.products.ProductDetailsScreen
import com.example.inventorymanagementapp.ui.products.ProductEditScreen
import com.example.inventorymanagementapp.ui.products.ProductListScreen
import com.example.inventorymanagementapp.ui.stock.StockManagementScreen
import com.example.inventorymanagementapp.ui.supplier.SupplierDetailsScreen
import com.example.inventorymanagementapp.ui.supplier.SupplierListScreen
import com.example.inventorymanagementapp.ui.transaction.TransactionDetailsScreen
import com.example.inventorymanagementapp.ui.transaction.TransactionListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToProducts = {
                    navController.navigate(Screen.ProductList.route)
                },
                onNavigateToTransaction = {
                    navController.navigate(Screen.TransactionList.route)
                },
                onLogoutClicked =  {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ProductList.route) {
            ProductListScreen(
                onProductClicked = { productId ->
                    navController.navigate(
                        Screen.ProductDetails.createRoute(productId)
                    )
                },
                onAddProductClicked = {
                    navController.navigate(Screen.ProductEdit.route)
                }
            )
        }

        composable(
            route = Screen.ProductDetails.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.LongType }
            )
        ) {
            ProductDetailsScreen(
                onEditClicked = { productId ->
                    navController.navigate(
                        Screen.ProductEdit.createRoute(productId)
                    )
                }
            )
        }

        composable(
            route = Screen.ProductEdit.route,
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.LongType
                    defaultValue = -1
                }
            )
        ) {
            ProductEditScreen(
                onFinished = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.SupplierList.route) {
            SupplierListScreen(
                onSupplierClicked = { supplierId ->
                    navController.navigate(
                        Screen.SupplierDetails.createRoute(supplierId)
                    )
                }
            )
        }

        composable(
            route = Screen.SupplierDetails.route,
            arguments = listOf(
                navArgument("supplierId") { type = NavType.LongType }
            )
        ) {
            SupplierDetailsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.TransactionList.route) {
            TransactionListScreen(
                onTransactionClicked = { transactionId ->
                    navController.navigate(Screen.TransactionDetails.createRoute(
                        transactionId
                    ))
                }
            )
        }

        composable(
            route = Screen.TransactionDetails.route,
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.LongType
                    defaultValue = -1
                }
            )
        ) {
            TransactionDetailsScreen()
        }

        composable(Screen.StockManagement.route) {
            StockManagementScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}