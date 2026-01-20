package com.example.inventorymanagementapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWithProductEntity(
    @Embedded val transactionEntity: InventoryTransactionEntity,

    @Relation(
        parentColumn = "product_id",
        entityColumn = "id"
    )
    val productEntity: ProductEntity
)
