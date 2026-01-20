package com.example.inventorymanagementapp.fakes

import com.example.inventorymanagementapp.domain.model.InventoryTransaction
import com.example.inventorymanagementapp.domain.model.TransactionWithProduct
import com.example.inventorymanagementapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FakeTransactionRepository @Inject constructor() : TransactionRepository {
    override fun getAllTransactions(): Flow<List<InventoryTransaction>> {
        TODO("Not yet implemented")
    }

    override fun getAllTransactionWithProductByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionWithProduct>> {
        TODO("Not yet implemented")
    }

    override fun getAllTransactionByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<InventoryTransaction>> {
        TODO("Not yet implemented")
    }

    override fun getTransactionById(id: Long): Flow<InventoryTransaction?> {
        TODO("Not yet implemented")
    }

    override fun getTransactionForProduct(productId: Long): Flow<List<TransactionWithProduct>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertTransaction(transaction: InventoryTransaction) {
        TODO("Not yet implemented")
    }
}