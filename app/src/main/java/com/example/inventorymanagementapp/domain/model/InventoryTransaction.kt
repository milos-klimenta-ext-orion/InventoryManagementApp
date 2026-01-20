package com.example.inventorymanagementapp.domain.model

data class InventoryTransaction(
    val id: Long = 0,
    val date: Long = System.currentTimeMillis(),
    val type: TransactionType,
    val productId: Long,
    val quantity: Int,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class TransactionType {
    RESTOCK, SALE
}