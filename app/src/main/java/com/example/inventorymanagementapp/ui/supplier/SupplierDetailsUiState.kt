package com.example.inventorymanagementapp.ui.supplier

import com.example.inventorymanagementapp.domain.model.Supplier

data class SupplierDetailsUiState(
    val supplier: Supplier? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

