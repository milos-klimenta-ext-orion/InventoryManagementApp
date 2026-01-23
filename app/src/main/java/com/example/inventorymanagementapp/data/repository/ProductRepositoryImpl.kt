package com.example.inventorymanagementapp.data.repository

import com.example.inventorymanagementapp.data.local.dao.ProductDao
import com.example.inventorymanagementapp.domain.mapper.toDomain
import com.example.inventorymanagementapp.domain.mapper.toEntity
import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { it.map { entity -> entity.toDomain() } }
    }

    override suspend fun updateStock(productId: Long, newStock: Int) {
        productDao.updateStock(productId, newStock)
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query).map {
            it.map { entity ->
                entity.toDomain()
            }
        }
    }

    override fun getLowStockProducts(): Flow<List<Product>> {
        return productDao.getLowStockProducts().map { it.map { entity -> entity.toDomain() } }
    }

    override suspend fun getProductById(id: Long): Product? {
        return productDao.getProductById(id)?.toDomain()
    }

    override suspend fun upsertProduct(product: Product) {
        productDao.upsertProduct(product.toEntity())
    }

    override suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product.toEntity())
    }
}