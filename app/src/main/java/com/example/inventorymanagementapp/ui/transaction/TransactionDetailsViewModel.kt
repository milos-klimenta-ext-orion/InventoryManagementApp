package com.example.inventorymanagementapp.ui.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapp.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TransactionDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val transactionId: Long = savedStateHandle["transactionId"] ?: -1

    val uiState: StateFlow<TransactionDetailsUiState> = transactionRepository
        .getTransactionById(transactionId)
        .map { transaction ->
            TransactionDetailsUiState(
                transaction = transaction,
                isLoading = transaction == null
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TransactionDetailsUiState())
}