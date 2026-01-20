package com.example.inventorymanagementapp.domain.repository

import com.example.inventorymanagementapp.domain.model.InventoryTransaction
import com.example.inventorymanagementapp.domain.model.TransactionWithProduct
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    fun getAllTransactions(): Flow<List<InventoryTransaction>>

    fun getAllTransactionWithProductByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionWithProduct>>

    fun getAllTransactionByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<InventoryTransaction>>

    fun getTransactionById(id: Long): Flow<InventoryTransaction?>

    fun getTransactionForProduct(productId: Long): Flow<List<TransactionWithProduct>>

    suspend fun insertTransaction(transaction: InventoryTransaction)
}