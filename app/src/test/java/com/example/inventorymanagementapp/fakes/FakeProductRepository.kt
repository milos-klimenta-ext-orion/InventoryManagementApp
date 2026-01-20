package com.example.inventorymanagementapp.fakes

import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeProductRepository : ProductRepository {

    private val productsFlow = MutableStateFlow<List<Product>>(emptyList())
    val upsertedProducts = mutableListOf<Product>()

    private val _lowStockProducts = MutableStateFlow<List<Product>>(emptyList())

    fun emitLowStockProducts(products: List<Product>) {
        _lowStockProducts.value = products
    }

    fun emitProducts(products: List<Product>) {
        productsFlow.value = products
    }

    override fun getAllProducts(): Flow<List<Product>> = productsFlow

    override fun searchProducts(query: String): Flow<List<Product>> =
        flowOf(emptyList())

    override fun getLowStockProducts(): Flow<List<Product>> =
        _lowStockProducts

    override suspend fun getProductById(id: Long): Product? =
        productsFlow.value.firstOrNull { it.id == id }

    override suspend fun upsertProduct(product: Product) {
        upsertedProducts.add(product)
        productsFlow.value = productsFlow.value
            .filterNot { it.id == product.id } + product
    }

    override suspend fun deleteProduct(product: Product) {}
}
