package com.example.inventorymanagementapp.ui.stock

import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.domain.model.TransactionType

data class StockManagementUiState(
    val products: List<Product> = emptyList(),
    val selectedProduct: Product? = null,
    val transactionType: TransactionType = TransactionType.RESTOCK,
    val quantity: String = "",
    val notes: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

