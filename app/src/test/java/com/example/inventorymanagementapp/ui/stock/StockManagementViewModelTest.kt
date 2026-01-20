package com.example.inventorymanagementapp.ui.stock

import com.example.inventorymanagementapp.MainDispatcherRule
import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.domain.model.TransactionType
import com.example.inventorymanagementapp.fakes.FakeProductRepository
import com.example.inventorymanagementapp.fakes.FakeTransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StockManagementViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var productRepository: FakeProductRepository
    private lateinit var transactionRepository: FakeTransactionRepository
    private lateinit var viewModel: StockManagementViewModel

    private val products = listOf(
        Product(1, "Milk", "", 2.5, "Food", "111", 1, 10, 2),
        Product(2, "Bread", "", 1.5, "Food", "222", 1, 5, 1)
    )

    @Before
    fun setup() {
        productRepository = FakeProductRepository()
        transactionRepository = FakeTransactionRepository()
    }

    private fun createViewModel() {
        viewModel = StockManagementViewModel(productRepository, transactionRepository)
        productRepository.emitProducts(products)
        runTest { advanceUntilIdle() } // wait for init{} loadProducts()
    }

    @Test
    fun initialStateLoadsProducts() = runTest {
        createViewModel()

        val state = viewModel.uiState.value

        assertEquals(2, state.products.size)
        assertFalse(state.isLoading)
    }

    @Test
    fun selectProductUpdatesSelectedProduct() = runTest {
        createViewModel()

        viewModel.selectProduct(products[0])
        val state = viewModel.uiState.value

        assertEquals(products[0], state.selectedProduct)
    }

    @Test
    fun saveTransactionWithInvalidQuantitySetsError() = runTest {
        createViewModel()
        viewModel.selectProduct(products[0])
        viewModel.setQuantity("abc") // invalid number

        viewModel.saveTransaction()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Invalid quantity", state.error)
    }

    @Test
    fun saveTransactionSaleWithInsufficientStockSetsError() = runTest {
        createViewModel()
        viewModel.selectProduct(products[1]) // stock = 5
        viewModel.setTransactionType(TransactionType.SALE)
        viewModel.setQuantity("10") // more than stock

        viewModel.saveTransaction()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Not enough stock", state.error)
    }

    @Test
    fun saveTransactionRestockUpdatesStockAndSuccess() = runTest {
        createViewModel()
        viewModel.selectProduct(products[1])
        viewModel.setTransactionType(TransactionType.RESTOCK)
        viewModel.setQuantity("5")

        viewModel.saveTransaction()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.success)
        val updatedProduct = productRepository.getAllProducts().first().first { it.id == products[1].id }
        assertEquals(10, updatedProduct.currentStock)
        assertEquals(1, transactionRepository.transactions.size)
    }

    @Test
    fun saveTransactionSaleDecreasesStockAndSuccess() = runTest {
        createViewModel()
        viewModel.selectProduct(products[0])
        viewModel.setTransactionType(TransactionType.SALE)
        viewModel.setQuantity("3")

        viewModel.saveTransaction()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.success)
        val updatedProduct = productRepository.getAllProducts().first().first { it.id == products[0].id }
        assertEquals(7, updatedProduct.currentStock)
        assertEquals(1, transactionRepository.transactions.size)
    }
}