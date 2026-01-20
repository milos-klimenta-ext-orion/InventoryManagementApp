package com.example.inventorymanagementapp.ui.dashboard

import com.example.inventorymanagementapp.MainDispatcherRule
import com.example.inventorymanagementapp.domain.model.InventoryTransaction
import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.domain.model.TransactionType
import com.example.inventorymanagementapp.fakes.FakeProductRepository
import com.example.inventorymanagementapp.fakes.FakeTransactionRepository
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var productRepository: FakeProductRepository
    private lateinit var transactionRepository: FakeTransactionRepository
    private lateinit var viewModel: DashboardViewModel

    private val lowStockProducts = listOf(
        Product(
            id = 1,
            name = "Milk",
            description = null,
            price = 1.5,
            category = "Food",
            barcode = "123456",
            supplierId = 0L,
            currentStock = 2,
            minimumStock = 5
        ),
        Product(
            id = 2,
            name = "Bread",
            description = null,
            price = 1.0,
            category = "Food",
            barcode = "999999",
            supplierId = 0L,
            currentStock = 1,
            minimumStock = 3
        )
    )

    private val transactions = listOf(
        InventoryTransaction(
            productId = 1,
            quantity = 2,
            type = TransactionType.SALE,
            notes = "",
            date = 1000L
        )
    )

    @Before
    fun setup() {
        productRepository = FakeProductRepository()
        transactionRepository = FakeTransactionRepository()
        viewModel = DashboardViewModel(productRepository, transactionRepository)
    }

    @Test
    fun initialStateIsLoading() = runTest {
        val state = viewModel.uiState.value

        assertTrue(state.isLoading)
        assertTrue(state.lowStockProducts.isEmpty())
        assertTrue(state.recentTransactions.isEmpty())
    }

    @Test
    fun uiStateShowsLowStockProductsAndRecentTransactions() = runTest {
        productRepository.emitLowStockProducts(lowStockProducts)
        transactionRepository.emitTransactions(transactions)

        val state = viewModel.uiState.first { !it.isLoading }

        assertEquals(2, state.lowStockProducts.size)
        assertEquals(1, state.recentTransactions.size)
        assertFalse(state.isLoading)
    }

    @Test
    fun updatesWhenLowStockProductsChange() = runTest {
        productRepository.emitLowStockProducts(lowStockProducts)
        transactionRepository.emitTransactions(transactions)

        viewModel.uiState.first { !it.isLoading }

        val updatedProducts = listOf(
            Product(
                id = 3,
                name = "Cheese",
                currentStock = 1,
                minimumStock = 4,
                price = 1.0,
                category = "Food",
                barcode = "999999",
                supplierId = 0L,
                description = null
            )
        )

        productRepository.emitLowStockProducts(updatedProducts)

        val state = viewModel.uiState.first { it.lowStockProducts.size == 1 }

        assertEquals("Cheese", state.lowStockProducts.first().name)
    }

    @Test
    fun updatesWhenTransactionsChange() = runTest {
        productRepository.emitLowStockProducts(lowStockProducts)
        transactionRepository.emitTransactions(transactions)

        viewModel.uiState.first { !it.isLoading }

        val newTransaction = InventoryTransaction(
            productId = 2,
            quantity = 5,
            type = TransactionType.RESTOCK,
            notes = "",
            date = 2000L
        )

        transactionRepository.emitTransactions(transactions + newTransaction)

        val state = viewModel.uiState.first { it.recentTransactions.size == 2 }

        assertEquals(2, state.recentTransactions.size)
    }
}