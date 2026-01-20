package com.example.inventorymanagementapp.ui.products

data class ProductEditUiState(
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val category: String = "",
    val barcode: String = "",
    val supplierId: Long = 0,
    val stock: String = "",
    val minStock: String = "",
    val isEditMode: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)
