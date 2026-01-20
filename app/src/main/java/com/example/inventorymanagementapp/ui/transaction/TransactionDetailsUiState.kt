package com.example.inventorymanagementapp.ui.transaction

import com.example.inventorymanagementapp.domain.model.InventoryTransaction

data class TransactionDetailsUiState(
    val transaction: InventoryTransaction? = null,
    val isLoading: Boolean = true
)
