package com.example.inventorymanagementapp.domain.mapper

import com.example.inventorymanagementapp.data.local.entity.InventoryTransactionEntity
import com.example.inventorymanagementapp.data.local.entity.TransactionWithProductEntity
import com.example.inventorymanagementapp.domain.model.InventoryTransaction
import com.example.inventorymanagementapp.domain.model.TransactionWithProduct

fun InventoryTransactionEntity.toDomain(): InventoryTransaction =
    InventoryTransaction(
        id = id,
        date = date,
        type = type,
        productId = productId,
        quantity = quantity,
        notes = notes,
        createdAt = createdAt
    )

fun InventoryTransaction.toEntity(): InventoryTransactionEntity =
    InventoryTransactionEntity(
        id = id,
        date = date,
        type = type,
        productId = productId,
        quantity = quantity,
        notes = notes,
        createdAt = createdAt
    )

fun TransactionWithProductEntity.toDomain(): TransactionWithProduct =
    TransactionWithProduct(
        transaction = transactionEntity.toDomain(),
        product = productEntity.toDomain()
    )