package com.example.inventorymanagementapp.ui.products

import androidx.lifecycle.SavedStateHandle
import com.example.inventorymanagementapp.MainDispatcherRule
import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.fakes.FakeProductRepository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeProductRepository
    private lateinit var viewModel: ProductDetailsViewModel

    @Before
    fun setup() {
        repository = FakeProductRepository()
    }

    private fun createViewModel(productId: Long) {
        val savedStateHandle = SavedStateHandle(mapOf("productId" to productId))
        viewModel = ProductDetailsViewModel(savedStateHandle, repository)
    }

    @Test
    fun loadProductSetsUiStateCorrectly() = runTest {
        val product = Product(
            id = 1,
            name = "Milk",
            description = "Fresh milk",
            price = 2.5,
            category = "Food",
            barcode = "123",
            supplierId = 10,
            currentStock = 5,
            minimumStock = 1
        )

        repository.emitProducts(listOf(product))

        createViewModel(1)
        advanceUntilIdle() // let init{} load

        val state = viewModel.uiState.value

        assertEquals(product, state.product)
        assertFalse(state.isLoading)
    }

    @Test
    fun productNotFoundSetsUiStateWithNull() = runTest {
        repository.emitProducts(emptyList())

        createViewModel(1)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertNull(state.product)
        assertFalse(state.isLoading)
    }

}