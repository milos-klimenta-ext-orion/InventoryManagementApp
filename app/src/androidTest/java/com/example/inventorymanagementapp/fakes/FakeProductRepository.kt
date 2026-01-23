package com.example.inventorymanagementapp.fakes

import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FakeProductRepository @Inject constructor() : ProductRepository {

    private val _products = MutableStateFlow<List<Product>>(emptyList())

    fun emitProducts(list: List<Product>) {
        _products.value = list
    }

    override fun getAllProducts(): Flow<List<Product>> = _products

    override suspend fun updateStock(productId: Long, newStock: Int) {
        val updatedList = _products.value.map { product ->
            if (product.id == productId) {
                product.copy(currentStock = newStock)
            } else {
                product
            }
        }
        _products.value = updatedList
    }

    override fun searchProducts(query: String): Flow<List<Product>> =
        _products.map { list ->
            list.filter { it.name.contains(query, ignoreCase = true) || it.barcode?.contains(query) == true }
        }

    override fun getLowStockProducts(): Flow<List<Product>> =
        _products.map { list -> list.filter { it.currentStock <= it.minimumStock } }

    override suspend fun getProductById(id: Long): Product? =
        _products.value.firstOrNull { it.id == id }

    override suspend fun upsertProduct(product: Product) {
        val current = _products.value.toMutableList()
        val index = current.indexOfFirst { it.id == product.id }
        if (index >= 0) current[index] = product else current.add(product)
        _products.value = current
    }

    override suspend fun deleteProduct(product: Product) {
        _products.value = _products.value.filter { it.id != product.id }
    }
}
