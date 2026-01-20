package com.example.inventorymanagementapp.domain.model

data class TransactionWithProduct(
    val transaction: InventoryTransaction,
    val product: Product
)
