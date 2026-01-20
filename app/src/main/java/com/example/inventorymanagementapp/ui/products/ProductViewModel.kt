package com.example.inventorymanagementapp.ui.products

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.domain.repository.ProductRepository
import com.example.inventorymanagementapp.util.Util.generateInventoryCsv
import com.example.inventorymanagementapp.util.Util.saveCsvToFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    // Combined flow: all products + search query
    val uiState: StateFlow<ProductListUiState> = combine(
        productRepository.getAllProducts(),
        _searchQuery
    ) { products, query ->
        val filtered = if (query.isBlank()) products
        else products.filter {
            it.name.contains(query, ignoreCase = true) || it.barcode?.contains(query) == true
        }
        ProductListUiState(products = filtered, searchQuery = query)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        ProductListUiState()
    )

    fun exportInventory(context: Context) {
        viewModelScope.launch {
            val products = productRepository.getAllProducts().first()
            val csv = generateInventoryCsv(products)
            saveCsvToFile(context, csv, "inventory_report.csv")
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setBarcodeQuery(barcode: String) {
        _searchQuery.value = barcode
    }
}