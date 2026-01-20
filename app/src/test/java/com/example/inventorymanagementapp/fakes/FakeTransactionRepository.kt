package com.example.inventorymanagementapp.fakes

import android.R.attr.end
import android.R.attr.start
import com.example.inventorymanagementapp.domain.model.InventoryTransaction
import com.example.inventorymanagementapp.domain.model.TransactionWithProduct
import com.example.inventorymanagementapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlin.collections.filter

class FakeTransactionRepository : TransactionRepository {

    val transactions = mutableListOf<InventoryTransaction>()

    private val _transactions = MutableStateFlow<List<InventoryTransaction>>(emptyList())

    fun emitTransactions(list: List<InventoryTransaction>) {
        _transactions.value = list
    }
    override fun getAllTransactions(): Flow<List<InventoryTransaction>> = _transactions

    override fun getAllTransactionWithProductByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionWithProduct>> {
        TODO("Not yet implemented")
    }

    override fun getAllTransactionByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<InventoryTransaction>> =
        _transactions.map { list -> list.filter { it.date in startDate..endDate } }


    override fun getTransactionById(id: Long): Flow<InventoryTransaction?> {
        return _transactions.map { list -> list.firstOrNull { it.id == id } }
    }

    override fun getTransactionForProduct(productId: Long): Flow<List<TransactionWithProduct>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertTransaction(transaction: InventoryTransaction) {
        transactions.add(transaction)
        _transactions.value = _transactions.value + transactions
    }
}
