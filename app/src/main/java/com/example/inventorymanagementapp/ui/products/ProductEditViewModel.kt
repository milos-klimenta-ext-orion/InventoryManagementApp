package com.example.inventorymanagementapp.ui.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val productId: Long? =
        savedStateHandle.get<Long>("productId").takeIf {it != -1L}

    private val _uiState = MutableStateFlow(ProductEditUiState())
    val uiState: StateFlow<ProductEditUiState> = _uiState

    init {
        if (productId != null) {
            loadProduct(productId)
        }
    }

    private fun loadProduct(id: Long) {
        viewModelScope.launch {
            val product = productRepository.getProductById(id) ?: return@launch

            _uiState.update {
                it.copy(
                    name = product.name,
                    description = product.description.orEmpty(),
                    price = product.price.toString(),
                    category = product.category ?: "",
                    barcode = product.barcode ?: "",
                    supplierId = product.supplierId,
                    stock = product.currentStock.toString(),
                    minStock = product.minimumStock.toString(),
                    isEditMode = true
                )
            }
        }
    }

    fun saveProduct() {
        viewModelScope.launch {
            val state = _uiState.value

            val barcode = state.barcode.trim()
            if (barcode.isNotEmpty()) {
                val allProducts = productRepository.getAllProducts().first() // get current products
                val existing = allProducts.firstOrNull { it.barcode == barcode && it.id != (productId ?: 0) }
                if (existing != null) {
                    _uiState.update { it.copy(error = "Product with this barcode already exists") }
                    return@launch
                }
            }

            val product = Product(
                id = productId ?: 0,
                name = state.name,
                description = state.description,
                price = state.price.toDouble(),
                category = state.category,
                barcode = state.barcode,
                supplierId = state.supplierId,
                currentStock = state.stock.toInt(),
                minimumStock = state.minStock.toInt()
            )

            productRepository.upsertProduct(product)

            _uiState.update { it.copy(isSaved = true) }
        }
    }

    fun onNameChange(value: String) =
        _uiState.update { it.copy(name = value) }

    fun onDescriptionChange(value: String) =
        _uiState.update { it.copy(description = value) }

    fun onPriceChange(value: String) =
        _uiState.update { it.copy(price = value) }

    fun onCategoryChange(value: String) =
        _uiState.update { it.copy(category = value) }

    fun onBarcodeChange(value: String) =
        _uiState.update { it.copy(barcode = value) }

    fun onSupplierIdChange(value: Long) =
        _uiState.update { it.copy(supplierId = value) }

    fun onCurrentStockChange(value: String) =
        _uiState.update { it.copy(stock = value) }

    fun onMinStockChange(value: String) =
        _uiState.update { it.copy(minStock = value) }
}