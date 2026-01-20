package com.example.inventorymanagementapp.ui.products

import com.example.inventorymanagementapp.domain.model.Product

data class ProductListUiState(
    val products: List<Product> = emptyList(),
    val searchQuery: String = ""
)
