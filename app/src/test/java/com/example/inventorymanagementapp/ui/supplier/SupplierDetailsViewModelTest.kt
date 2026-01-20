package com.example.inventorymanagementapp.ui.supplier

import androidx.lifecycle.SavedStateHandle
import com.example.inventorymanagementapp.MainDispatcherRule
import com.example.inventorymanagementapp.domain.model.Supplier
import com.example.inventorymanagementapp.fakes.FakeSupplierRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class SupplierDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeSupplierRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: SupplierDetailsViewModel

    private val supplier = Supplier(
        id = 1,
        name = "Acme Inc.",
        contactPerson = "John",
        phone = "123456",
        email = "acme@example.com",
        address = "Street 1"
    )

    @Before
    fun setup() {
        repository = FakeSupplierRepository()
        savedStateHandle = SavedStateHandle(mapOf("supplierId" to 1L))
    }

    private fun createViewModel() {
        viewModel = SupplierDetailsViewModel(savedStateHandle, repository)
    }

    @Test
    fun initialStateShowsLoadingThenSupplier() = runTest {
        repository.emitSuppliers(listOf(supplier))
        createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(supplier, state.supplier)
        assertNull(state.error)
    }

    @Test
    fun ifSupplierNotFoundShowError() = runTest {
        repository.emitSuppliers(emptyList())
        createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.supplier)
        assertEquals("Failed to load supplier", state.error)
    }
}