package com.example.inventorymanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.inventorymanagementapp.data.local.entity.InventoryTransactionEntity
import com.example.inventorymanagementapp.data.local.entity.TransactionWithProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    // CREATE
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTransaction(transaction: InventoryTransactionEntity): Long

    // READ
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<InventoryTransactionEntity>>

    // Get single transaction by its ID
    @Query("SELECT * FROM transactions WHERE id = :transactionId LIMIT 1")
    fun getTransactionById(transactionId: Long): Flow<InventoryTransactionEntity?>

    @Transaction
    @Query("""
        SELECT * FROM transactions
        WHERE date BETWEEN :startDate AND :endDate
        ORDER BY date DESC
    """)
    fun getTransactionsByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<InventoryTransactionEntity>>

    @Transaction
    @Query("""
        SELECT * FROM transactions
        WHERE date BETWEEN :startDate AND :endDate
        ORDER BY date DESC
    """)
    fun getTransactionsWithProductByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionWithProductEntity>>

    // FILTER BY PRODUCT
    @Transaction
    @Query("""
        SELECT * FROM transactions
        WHERE product_id = :productId
        ORDER BY date DESC
    """)
    fun getTransactionsForProduct(
        productId: Long
    ): Flow<List<TransactionWithProductEntity>>
}