package com.example.inventorymanagementapp.ui.supplier

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapp.domain.repository.SupplierRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val supplierRepository: SupplierRepository
): ViewModel() {

    private val supplierId: Long =
        checkNotNull(savedStateHandle["supplierId"])

    private val _uiState = MutableStateFlow(SupplierDetailsUiState())
    val uiState: StateFlow<SupplierDetailsUiState> = _uiState

    init {
        loadSupplier()
    }

    private fun loadSupplier() {
        viewModelScope.launch {
            try {
                val supplier = supplierRepository.getSupplierById(supplierId)

                _uiState.value = SupplierDetailsUiState(
                    supplier = supplier,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = SupplierDetailsUiState(
                    isLoading = false,
                    error = "Failed to load supplier"
                )
            }
        }
    }
}