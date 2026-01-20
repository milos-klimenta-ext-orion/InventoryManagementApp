package com.example.inventorymanagementapp.fakes

import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.ui.products.ProductListUiState
import com.example.inventorymanagementapp.ui.products.ProductViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeProductViewModel{

//    private val _uiState = MutableStateFlow(
//        ProductListUiState(
//            products = listOf(
//                Product(1, "Milk", "1L Milk", 1.2, "Dairy", "123", 1, 5, 2),
//                Product(2, "Bread", "Whole Wheat", 0.8, "Bakery", "999", 2, 10, 5)
//            )
//        )
//    )
//
//    override val uiState: StateFlow<ProductListUiState> = _uiState
//
//    fun setSearchQuery(query: String) {
//        _uiState.value = _uiState.value.copy(searchQuery = query)
//    }
}