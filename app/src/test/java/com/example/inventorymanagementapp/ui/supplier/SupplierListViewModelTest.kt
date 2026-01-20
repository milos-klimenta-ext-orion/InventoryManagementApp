package com.example.inventorymanagementapp.ui.supplier

import com.example.inventorymanagementapp.MainDispatcherRule
import com.example.inventorymanagementapp.domain.model.Supplier
import com.example.inventorymanagementapp.fakes.FakeSupplierRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SupplierListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeSupplierRepository
    private lateinit var viewModel: SupplierListViewModel

    private val suppliers = listOf(
        Supplier(1, "Acme Inc.", "Peter", "12345678", "peter@test", ""),
        Supplier(2, "Global Corp", "Milos", "12345678", "milos@test", "")
    )

    @Before
    fun setup() {
        repository = FakeSupplierRepository()
    }

    private fun createViewModel() {
        viewModel = SupplierListViewModel(repository)
        repository.emitSuppliers(suppliers)
        runTest { advanceUntilIdle() }
    }

    @Test
    fun initialStateShowsAllSuppliers() = runTest {
        createViewModel()

        val state = viewModel.uiState.value
        assertEquals(2, state.suppliers.size)
        assertFalse(state.isLoading)
    }

    @Test
    fun searchFiltersSuppliersByName() = runTest {
        createViewModel()

        viewModel.setSearchQuery("Acme")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.suppliers.size)
        assertEquals("Acme Inc.", state.suppliers.first().name)
    }

    @Test
    fun emptySearchShowsAllSuppliers() = runTest {
        createViewModel()

        viewModel.setSearchQuery("")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, state.suppliers.size)
    }

    @Test
    fun searchWithNoMatchReturnsEmptyList() = runTest {
        createViewModel()

        viewModel.setSearchQuery("Nonexistent")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.suppliers.isEmpty())
    }
}