package com.example.inventorymanagementapp.ui.transaction

import com.example.inventorymanagementapp.domain.model.InventoryTransaction

data class TransactionListUiState(
    val transactions: List<InventoryTransaction> = emptyList(),
    val isLoading: Boolean = false
)
