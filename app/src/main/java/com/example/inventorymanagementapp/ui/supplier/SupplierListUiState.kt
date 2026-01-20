package com.example.inventorymanagementapp.ui.supplier

import com.example.inventorymanagementapp.domain.model.Supplier

data class SupplierListUiState(
    val suppliers: List<Supplier> = emptyList(),
    val isLoading: Boolean = true
)
