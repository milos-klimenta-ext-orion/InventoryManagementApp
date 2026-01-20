package com.example.inventorymanagementapp.ui.products

import androidx.lifecycle.SavedStateHandle
import com.example.inventorymanagementapp.MainDispatcherRule
import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.fakes.FakeProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class ProductEditViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeProductRepository
    private lateinit var viewModel: ProductEditViewModel

    private fun createViewModel(productId: Long? = null) {
        val savedStateHandle = SavedStateHandle(
            mapOf("productId" to productId)
        )
        viewModel = ProductEditViewModel(savedStateHandle, repository)
    }

    @Before
    fun setup() {
        repository = FakeProductRepository()
    }

    @Test
    fun createModeStartsWithEmptyState() = runTest {
        createViewModel(null)

        val state = viewModel.uiState.value

        assertFalse(state.isEditMode)
        assertEquals("", state.name)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun editModeLoadsProduct() = runTest {
        val product = Product(
            id = 1,
            name = "Milk",
            description = "Fresh",
            price = 2.5,
            category = "Food",
            barcode = "123",
            supplierId = 10,
            currentStock = 5,
            minimumStock = 1
        )
        repository.emitProducts(listOf(product))

        createViewModel(productId = 1)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state.isEditMode)
        assertEquals("Milk", state.name)
        assertEquals("123", state.barcode)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveNewProductSucceeds() = runTest {
        createViewModel(null)

        viewModel.onNameChange("Bread")
        viewModel.onPriceChange("1.2")
        viewModel.onBarcodeChange("999")
        viewModel.onCurrentStockChange("3")
        viewModel.onMinStockChange("1")

        viewModel.saveProduct()
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state.isSaved)
        assertEquals(1, repository.upsertedProducts.size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun duplicateBarcodeShowsErrorAndDoesNotSave() = runTest {
        repository.emitProducts(
            listOf(
                Product(
                    id = 1,
                    name = "Milk",
                    description = null,
                    price = 2.0,
                    category = null,
                    barcode = "123",
                    supplierId = 0L,
                    currentStock = 5,
                    minimumStock = 1
                )
            )
        )

        createViewModel(null)

        viewModel.onNameChange("Bread")
        viewModel.onPriceChange("1.5")
        viewModel.onBarcodeChange("123")
        viewModel.onCurrentStockChange("2")
        viewModel.onMinStockChange("1")

        viewModel.saveProduct()
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals("Product with this barcode already exists", state.error)
        assertFalse(state.isSaved)
        assertTrue(repository.upsertedProducts.isEmpty())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun editingProductAllowsSameBarcode() = runTest {
        val product = Product(
            id = 1,
            name = "Milk",
            description = null,
            price = 2.0,
            category = null,
            barcode = "123",
            supplierId = 0L,
            currentStock = 5,
            minimumStock = 1
        )
        repository.emitProducts(listOf(product))

        createViewModel(productId = 1)
        advanceUntilIdle()

        viewModel.saveProduct()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isSaved)
    }

}