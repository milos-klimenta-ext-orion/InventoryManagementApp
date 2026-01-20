package com.example.inventorymanagementapp.data.repository

import com.example.inventorymanagementapp.data.local.dao.TransactionDao
import com.example.inventorymanagementapp.domain.mapper.toDomain
import com.example.inventorymanagementapp.domain.mapper.toEntity
import com.example.inventorymanagementapp.domain.model.InventoryTransaction
import com.example.inventorymanagementapp.domain.model.TransactionWithProduct
import com.example.inventorymanagementapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
): TransactionRepository {

    override fun getAllTransactions(): Flow<List<InventoryTransaction>> {
        return transactionDao.getAllTransactions().map { it.map { entity -> entity.toDomain() } }
    }

    override fun getAllTransactionWithProductByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionWithProduct>> {
        return transactionDao.getTransactionsWithProductByDateRange(startDate, endDate)
            .map { it.map { entity ->
                entity.toDomain()
            } }
    }

    override fun getAllTransactionByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<InventoryTransaction>> {
        return transactionDao.getTransactionsByDateRange(startDate, endDate)
            .map { it.map { entity -> entity.toDomain() } }
    }

    override fun getTransactionById(id: Long): Flow<InventoryTransaction?> {
        return transactionDao.getTransactionById(id)
            .map { it?.toDomain() }
    }

    override fun getTransactionForProduct(productId: Long): Flow<List<TransactionWithProduct>> {
        return transactionDao.getTransactionsForProduct(productId).map {
            it.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun insertTransaction(transaction: InventoryTransaction) {
        transactionDao.insertTransaction(transaction.toEntity())
    }
}