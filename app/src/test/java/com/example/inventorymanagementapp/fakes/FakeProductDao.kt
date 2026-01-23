package com.example.inventorymanagementapp.fakes

import com.example.inventorymanagementapp.data.local.dao.ProductDao
import com.example.inventorymanagementapp.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeProductDao : ProductDao {

    private val products = mutableListOf<ProductEntity>()
    private val flow = MutableStateFlow<List<ProductEntity>>(emptyList())

    fun emit(list: List<ProductEntity>) {
        products.clear()
        products.addAll(list)
        flow.value = list
    }

    override fun getAllProducts(): Flow<List<ProductEntity>> = flow

    override fun searchProducts(query: String): Flow<List<ProductEntity>> =
        flow.map {
            it.filter { p ->
                p.name.contains(query, ignoreCase = true) ||
                        p.barcode?.contains(query) == true
            }
        }

    override fun getLowStockProducts(): Flow<List<ProductEntity>> =
        flow.map { it.filter { p -> p.currentStock <= p.minimumStock } }

    override suspend fun getProductById(id: Long): ProductEntity? =
        products.firstOrNull { it.id == id }

    override suspend fun upsertProduct(product: ProductEntity): Long {
        products.removeAll { it.id == product.id }
        products.add(product)
        flow.value = products.toList()
        return 1L
    }

    override suspend fun updateStock(productId: Long, newStock: Int) {
        val index = products.indexOfFirst { it.id == productId }
        if (index != -1) {
            val old = products[index]
            val updated = old.copy(currentStock = newStock)
            products[index] = updated
            flow.value = products.toList()
        }
    }

    override suspend fun deleteProduct(product: ProductEntity) {
        products.removeIf { it.id == product.id }
        flow.value = products.toList()
    }
}
