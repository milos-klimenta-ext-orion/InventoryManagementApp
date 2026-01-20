package com.example.inventorymanagementapp.ui.transaction

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapp.domain.model.TransactionType
import com.example.inventorymanagementapp.domain.repository.TransactionRepository
import com.example.inventorymanagementapp.util.Util.generateTransactionCsv
import com.example.inventorymanagementapp.util.Util.saveCsvToFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _transactionType = MutableStateFlow<TransactionType?>(null)
    private val _startDate = MutableStateFlow<Long?>(null)
    private val _endDate = MutableStateFlow<Long?>(null)
    private val _uiState = MutableStateFlow(TransactionListUiState())
    val uiState: StateFlow<TransactionListUiState> = _uiState

    init {
        observeTransactions()
    }

    private fun observeTransactions() {
        combine(
            _startDate,
            _endDate,
            _transactionType
        ) { start, end, type ->
            Triple(start, end, type)
        }
            .flatMapLatest { (start, end, type) ->

                val startTime = start ?: 0L
                val endTime = end ?: Long.MAX_VALUE

                transactionRepository
                    .getAllTransactionByDateRange(startTime, endTime)
                    .map { transactions ->
                        transactions.filter { transaction ->
                            type == null || transaction.type == type
                        }
                    }
            }
            .onEach { filtered ->
                _uiState.value = TransactionListUiState(
                    transactions = filtered,
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }

    fun exportTransactionCSV(context: Context) {
        viewModelScope.launch {
            val transactions = transactionRepository.getAllTransactions().first()
            val csv = generateTransactionCsv(transactions)
            saveCsvToFile(context, csv, "transaction_report.csv")
        }
    }

    fun setTransactionType(type: TransactionType?) {
        _transactionType.value = type
    }

    fun setStartDate(start: Long?) {
        _startDate.value = start
    }

    fun setEndDate(end: Long?) {
        _endDate.value = end
    }
}