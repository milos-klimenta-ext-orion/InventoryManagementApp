package com.example.inventorymanagementapp.ui.products

import com.example.inventorymanagementapp.MainDispatcherRule
import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.fakes.FakeProductRepository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeProductRepository
    private lateinit var viewModel: ProductViewModel

    private val products = listOf(
        Product(
            id = 1,
            name = "Milk",
            description = null,
            price = 1.5,
            category = "Food",
            barcode = "123456",
            supplierId = 0L,
            currentStock = 10,
            minimumStock = 2
        ),
        Product(
            id = 2,
            name = "Bread",
            description = null,
            price = 1.0,
            category = "Food",
            barcode = "999999",
            supplierId = 0L,
            currentStock = 5,
            minimumStock = 1
        )
    )

    @Before
    fun setup() {
        repository = FakeProductRepository()
        viewModel = ProductViewModel(repository)
    }

    @Test
    fun initialStateShowsAllProducts() = runTest {
        repository.emitProducts(products)

        val job = launch {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(2, state.products.size)
        assertEquals("", state.searchQuery)

        job.cancel()
    }

    @Test
    fun searchByNameFiltersProducts() = runTest {
        repository.emitProducts(products)

        val job = launch { viewModel.uiState.collect() }

        viewModel.setSearchQuery("milk")
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(1, state.products.size)
        assertEquals("Milk", state.products.first().name)

        job.cancel()
    }

    @Test
    fun searchByBarcodeFilterProducts() = runTest {
        repository.emitProducts(products)

        val job = launch { viewModel.uiState.collect() }

        viewModel.setBarcodeQuery("999")
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(1, state.products.size)
        assertEquals("Bread", state.products.first().name)

        job.cancel()
    }

    @Test
    fun emptySearchShowsAllProducts() = runTest {
        repository.emitProducts(products)

        val job = launch { viewModel.uiState.collect() }

        viewModel.setSearchQuery("")
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(2, state.products.size)

        job.cancel()
    }

    @Test
    fun searchWithNoMatchReturnsEmptyList() = runTest {
        repository.emitProducts(products)

        val job = launch { viewModel.uiState.collect() }

        viewModel.setSearchQuery("nonexistent")
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state.products.isEmpty())

        job.cancel()
    }
}