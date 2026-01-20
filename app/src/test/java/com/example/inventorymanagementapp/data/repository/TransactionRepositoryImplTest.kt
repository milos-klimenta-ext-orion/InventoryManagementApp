package com.example.inventorymanagementapp.data.repository

import com.example.inventorymanagementapp.data.local.entity.InventoryTransactionEntity
import com.example.inventorymanagementapp.data.local.entity.ProductEntity
import com.example.inventorymanagementapp.data.local.entity.TransactionWithProductEntity
import com.example.inventorymanagementapp.domain.model.InventoryTransaction
import com.example.inventorymanagementapp.domain.model.TransactionType
import com.example.inventorymanagementapp.fakes.FakeTransactionDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionRepositoryImplTest {

    private lateinit var dao: FakeTransactionDao
    private lateinit var repository: TransactionRepositoryImpl

    private val transactionEntities = listOf(
        InventoryTransactionEntity(
            id = 1,
            productId = 10,
            quantity = 5,
            type = TransactionType.SALE,
            notes = "",
            date = 1000L
        ),
        InventoryTransactionEntity(
            id = 2,
            productId = 20,
            quantity = 10,
            type = TransactionType.RESTOCK,
            notes = "",
            date = 2000L
        )
    )

    private val transactionWithProductEntities = listOf(
        TransactionWithProductEntity(
            transactionEntity = transactionEntities[0],
            productEntity = ProductEntity(
                id = 10,
                name = "Product A",
                supplierId = 1,
                price = 1.0,
                currentStock = 10,
                minimumStock = 5
            )
        )
    )

    @Before
    fun setup() {
        dao = FakeTransactionDao()
        repository = TransactionRepositoryImpl(dao)
    }

    @Test
    fun getAllTransactionsMapsEntitiesToDomain() = runTest {
        dao.emitTransactions(transactionEntities)

        val result = repository.getAllTransactions().first()

        assertEquals(2, result.size)
        assertEquals(TransactionType.SALE, result.first().type)
    }

    @Test
    fun getTransactionByIdReturnsMappedTransaction() = runTest {
        dao.emitTransactions(transactionEntities)

        val result = repository.getTransactionById(1).first()

        assertNotNull(result)
        assertEquals(10, result.productId)
    }

    @Test
    fun getAllTransactionByDateRangeFiltersCorrectly() = runTest {
        dao.emitTransactions(transactionEntities)

        val result = repository
            .getAllTransactionByDateRange(1500L, 2500L)
            .first()

        assertEquals(1, result.size)
        assertEquals(2000L, result.first().date)
    }

    @Test
    fun getAllTransactionWithProductByDateRangeMapsCorrectly() = runTest {
        dao.emitTransactionWithProduct(transactionWithProductEntities)

        val result = repository
            .getAllTransactionWithProductByDateRange(0L, 3000L)
            .first()

        assertEquals(1, result.size)
        assertEquals("Product A", result.first().product.name)
    }

    @Test
    fun getTransactionForProductReturnsCorrectTransactions() = runTest {
        dao.emitTransactionWithProduct(transactionWithProductEntities)

        val result = repository.getTransactionForProduct(10).first()

        assertEquals(1, result.size)
        assertEquals(10, result.first().transaction.productId)
    }

    @Test
    fun insertTransactionStoresMappedEntity() = runTest {
        val transaction = InventoryTransaction(
            id = 3,
            productId = 30,
            quantity = 7,
            type = TransactionType.SALE,
            notes = "",
            date = 3000L
        )

        repository.insertTransaction(transaction)

        val stored = dao.getTransactionEntityById(3)

        assertNotNull(stored)
        assertEquals(7, stored.quantity)
    }
}