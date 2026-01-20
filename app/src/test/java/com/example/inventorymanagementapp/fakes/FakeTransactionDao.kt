package com.example.inventorymanagementapp.fakes

import com.example.inventorymanagementapp.data.local.dao.TransactionDao
import com.example.inventorymanagementapp.data.local.entity.InventoryTransactionEntity
import com.example.inventorymanagementapp.data.local.entity.TransactionWithProductEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class FakeTransactionDao : TransactionDao {

    private val transactionsFlow =
        MutableStateFlow<List<InventoryTransactionEntity>>(emptyList())

    private val transactionsWithProductFlow =
        MutableStateFlow<List<TransactionWithProductEntity>>(emptyList())

    fun emitTransactions(list: List<InventoryTransactionEntity>) {
        transactionsFlow.value = list
    }

    fun emitTransactionWithProduct(list: List<TransactionWithProductEntity>) {
        transactionsWithProductFlow.value = list
    }

    fun getTransactionEntityById(id: Long): InventoryTransactionEntity? {
        return transactionsFlow.value.firstOrNull { it.id == id }
    }

    override fun getAllTransactions(): Flow<List<InventoryTransactionEntity>> {
        return transactionsFlow
    }

    override fun getTransactionsByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<InventoryTransactionEntity>> {
        return transactionsFlow.map { list ->
            list.filter { it.date in startDate..endDate }
        }
    }

    override fun getTransactionById(transactionId: Long): Flow<InventoryTransactionEntity?> {
        return transactionsFlow.map { list ->
            list.firstOrNull { it.id == transactionId }
        }
    }

    override fun getTransactionsWithProductByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionWithProductEntity>> {
        return transactionsWithProductFlow.map { list ->
            list.filter { it.transactionEntity.date in startDate..endDate }
        }
    }

    override fun getTransactionsForProduct(
        productId: Long
    ): Flow<List<TransactionWithProductEntity>> {
        return transactionsWithProductFlow.map { list ->
            list.filter { it.transactionEntity.productId == productId }
        }
    }

    override suspend fun insertTransaction(transaction: InventoryTransactionEntity): Long {
        transactionsFlow.value = transactionsFlow.value + transaction
        return 1L
    }
}