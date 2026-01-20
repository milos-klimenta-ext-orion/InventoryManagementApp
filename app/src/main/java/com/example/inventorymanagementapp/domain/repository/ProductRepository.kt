package com.example.inventorymanagementapp.domain.repository

import com.example.inventorymanagementapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    fun searchProducts(query: String): Flow<List<Product>>
    fun getLowStockProducts(): Flow<List<Product>>
    suspend fun getProductById(id: Long): Product?
    suspend fun upsertProduct(product: Product)
    suspend fun deleteProduct(product: Product)
}