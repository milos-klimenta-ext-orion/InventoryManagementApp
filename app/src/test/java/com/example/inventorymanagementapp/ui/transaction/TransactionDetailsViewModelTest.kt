package com.example.inventorymanagementapp.ui.transaction

import androidx.lifecycle.SavedStateHandle
import com.example.inventorymanagementapp.MainDispatcherRule
import com.example.inventorymanagementapp.domain.model.InventoryTransaction
import com.example.inventorymanagementapp.domain.model.TransactionType
import com.example.inventorymanagementapp.fakes.FakeTransactionRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeTransactionRepository
    private lateinit var viewModel: TransactionDetailsViewModel

    private val transactions = listOf(
        InventoryTransaction(
            id = 1,
            productId = 1,
            quantity = 5,
            type = TransactionType.SALE,
            notes = "",
            date = 1000L
        ),
        InventoryTransaction(
            id = 2,
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
    }

    private fun createViewModel(transactionId: Long) {
        repository.emitTransactions(transactions)
        viewModel = TransactionDetailsViewModel(
            SavedStateHandle(mapOf("transactionId" to transactionId)),
            repository
        )
    }

    @Test
    fun uiStateShowsCorrectTransactionWheItExists() = runTest {
        createViewModel(1L)

        val state = viewModel.uiState.first { it.transaction != null }

        assertNotNull(state.transaction)
        assertEquals(1L, state.transaction.id)
        assertFalse(state.isLoading)
    }

    @Test
    fun uiStateIsLoadingWhenTransactionDoesNotExist() = runTest {
        createViewModel(999L) // non-existent ID

        val state = viewModel.uiState.value
        assertNull(state.transaction)
        assertTrue(state.isLoading)
    }
}