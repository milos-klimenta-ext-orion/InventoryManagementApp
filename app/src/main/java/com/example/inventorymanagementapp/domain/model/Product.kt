package com.example.inventorymanagementapp.domain.model

data class Product(
    val id: Long = 0,
    val name: String,
    val description: String?,
    val price: Double,
    val category: String?,
    val barcode: String?,
    val supplierId: Long,
    val currentStock: Int,
    val minimumStock: Int
)
