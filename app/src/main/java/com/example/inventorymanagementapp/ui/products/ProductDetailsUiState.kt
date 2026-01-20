package com.example.inventorymanagementapp.ui.products

import com.example.inventorymanagementapp.domain.model.Product

data class ProductDetailsUiState(
    val product: Product? = null,
    val isLoading: Boolean = true
)
