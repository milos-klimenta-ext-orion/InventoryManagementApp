package com.example.inventorymanagementapp.ui.transaction

import com.example.inventorymanagementapp.MainDispatcherRule
import com.example.inventorymanagementapp.domain.model.InventoryTransaction
import com.example.inventorymanagementapp.domain.model.TransactionType
import com.example.inventorymanagementapp.fakes.FakeTransactionRepository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)

class TransactionListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeTransactionRepository
    private lateinit var viewModel: TransactionListViewModel

    private val transactions = listOf(
        InventoryTransaction(
            productId = 1,
            quantity = 5,
            type = TransactionType.SALE,
            notes = "",
            date = 1000L
        ),
        InventoryTransaction(
            productId = 2,
            quantity = 10,
            type = TransactionType.RESTOCK,
            notes = "",
            date = 2000L
        )
    )

    @Before
    fun setup() {
        repository = FakeTransactionRepository()
        viewModel = TransactionListViewModel(repository)
    }

    @Test
    fun initialStateShowsAllTransactions() = runTest {
        repository.emitTransactions(transactions)

        val job = launch {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(2, state.transactions.size)
        assertFalse(state.isLoading)

        job.cancel()
    }

    @Test
    fun filterByTransactionType() = runTest {
        repository.emitTransactions(transactions)

        val job = launch {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()

        viewModel.setTransactionType(TransactionType.SALE)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.transactions.size)
        assertEquals(TransactionType.SALE, state.transactions.first().type)

        job.cancel()
    }

    @Test
    fun filterByStartDate() = runTest {
        repository.emitTransactions(transactions)

        val job = launch {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()

        viewModel.setStartDate(1500L)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.transactions.size)
        assertTrue(state.transactions.all { it.date >= 1500L })

        job.cancel()
    }

    @Test
    fun filterByEndDate() = runTest {
        repository.emitTransactions(transactions)

        val job = launch {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()

        viewModel.setEndDate(1500L)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.transactions.size)
        assertTrue(state.transactions.all { it.date <= 1500L })

        job.cancel()
    }

    @Test
    fun filterByTypeAndDateRange() = runTest {
        repository.emitTransactions(transactions)

        val job = launch {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()

        viewModel.setTransactionType(TransactionType.RESTOCK)
        viewModel.setStartDate(1500L)
        viewModel.setEndDate(2500L)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.transactions.size)
        val t = state.transactions.first()
        assertEquals(TransactionType.RESTOCK, t.type)
        assertTrue(t.date in 1500L..2500L)

        job.cancel()
    }
}