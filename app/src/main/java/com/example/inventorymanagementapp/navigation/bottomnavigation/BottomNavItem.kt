package com.example.inventorymanagementapp.navigation.bottomnavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.inventorymanagementapp.navigation.Screen

sealed class BottomNavItem(
    val screen: Screen,
    val title: String,
    val icon: ImageVector
) {
    object Dashboard : BottomNavItem(
        Screen.Dashboard,
        "Home",
        Icons.Default.Home
    )

    object Transactions : BottomNavItem(
        Screen.TransactionList,
        "Transaction",
        Icons.AutoMirrored.Filled.ReceiptLong
    )

    object Products : BottomNavItem(
        Screen.ProductList,
        "Products",
        Icons.Default.Inventory
    )

    object Suppliers : BottomNavItem(
        Screen.SupplierList,
        "Suppliers",
        Icons.Default.Business
    )

    object Stock : BottomNavItem(
        Screen.StockManagement,
        "Stock",
        Icons.Default.Warehouse
    )
}
