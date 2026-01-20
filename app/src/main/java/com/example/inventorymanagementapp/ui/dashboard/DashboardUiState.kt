package com.example.inventorymanagementapp.ui.dashboard

import com.example.inventorymanagementapp.domain.model.InventoryTransaction
import com.example.inventorymanagementapp.domain.model.Product

data class DashboardUiState(
    val lowStockProducts: List<Product> = emptyList(),
    val recentTransactions: List<InventoryTransaction> = emptyList(),
    val isLoading: Boolean = true
) {
    val isEmpty: Boolean
        get() = lowStockProducts.isEmpty() && recentTransactions.isEmpty()
}
