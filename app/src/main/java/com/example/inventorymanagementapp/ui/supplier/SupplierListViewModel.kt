package com.example.inventorymanagementapp.ui.supplier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapp.domain.repository.SupplierRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SupplierListViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository
): ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _uiState = MutableStateFlow(SupplierListUiState())
    val uiState: StateFlow<SupplierListUiState> = _uiState

    init {
        observeSuppliers()
    }

    private fun observeSuppliers() {
        combine(
            supplierRepository.getAllSuppliers(),
            _searchQuery
        ) { suppliers, query ->
            if (query.isBlank()) suppliers
            else suppliers.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }.onEach { filtered ->
            _uiState.value = SupplierListUiState(
                suppliers = filtered,
                isLoading = false
            )
        }.launchIn(viewModelScope)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}