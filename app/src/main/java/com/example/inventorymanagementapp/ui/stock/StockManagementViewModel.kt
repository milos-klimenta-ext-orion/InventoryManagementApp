package com.example.inventorymanagementapp.ui.stock

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapp.domain.model.InventoryTransaction
import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.domain.model.TransactionType
import com.example.inventorymanagementapp.domain.repository.ProductRepository
import com.example.inventorymanagementapp.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockManagementViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val transactionRepository: TransactionRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(StockManagementUiState(isLoading = true))
    val uiState: StateFlow<StockManagementUiState> = _uiState

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts()
                .collect { products ->
                    _uiState.update { it.copy(products = products, isLoading = false) }
                }
        }
    }

    fun saveTransaction() {
        val state = _uiState.value
        val product = state.selectedProduct ?: return

        val quantity = state.quantity.toIntOrNull()
        if (quantity == null || quantity <= 0) {
            _uiState.update { it.copy(error = "Invalid quantity") }
            return
        }

        if (
            state.transactionType == TransactionType.SALE &&
            product.currentStock < quantity
        ) {
            _uiState.update { it.copy(error = "Not enough stock") }
            return
        }

        viewModelScope.launch {
            val newStock =
                if (state.transactionType == TransactionType.RESTOCK)
                    product.currentStock + quantity
                else
                    product.currentStock - quantity

            val inventoryTransaction = InventoryTransaction(
                productId = product.id,
                quantity = quantity,
                type = state.transactionType,
                notes = state.notes
            )
            try {
                transactionRepository.insertTransaction(
                    inventoryTransaction
                )

                productRepository.updateStock(
                    product.id, newStock
                )
            } catch (e: Exception) {
                Log.e("klima", "Transaction insert failed", e)
            }

            _uiState.update { it.copy(success = true) }
        }
    }

    fun selectProduct(product: Product) {
        _uiState.update { it.copy(selectedProduct = product) }
    }

    fun setTransactionType(type: TransactionType) {
        _uiState.update { it.copy(transactionType = type) }
    }

    fun setQuantity(value: String) {
        _uiState.update { it.copy(quantity = value) }
    }

    fun setNotes(value: String) {
        _uiState.update { it.copy(notes = value) }
    }
}