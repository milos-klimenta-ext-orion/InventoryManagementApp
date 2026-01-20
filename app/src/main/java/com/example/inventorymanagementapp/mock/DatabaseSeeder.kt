package com.example.inventorymanagementapp.mock

import com.example.inventorymanagementapp.data.local.dao.ProductDao
import com.example.inventorymanagementapp.data.local.dao.TransactionDao
import com.example.inventorymanagementapp.domain.mapper.toEntity
import com.example.inventorymanagementapp.domain.model.InventoryTransaction
import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.domain.model.TransactionType
import javax.inject.Inject

class DatabaseSeeder @Inject constructor(
    private val productDao: ProductDao,
    private val transactionDao: TransactionDao
) {
    suspend fun seedIfNeeded() {
        val products = listOf(
            Product(
                name = "Milk",
                description = "1L Milk",
                price = 1.2,
                category = "Dairy",
                barcode = "123456",
                supplierId = 1,
                currentStock = 3,
                minimumStock = 5
            ),
            Product(
                name = "Bread",
                description = "White bread",
                price = 0.8,
                category = "Bakery",
                barcode = "654321",
                supplierId = 1,
                currentStock = 10,
                minimumStock = 5
            )
        )

        products.onEach { productDao.upsertProduct(it.toEntity()) }

        transactionDao.insertTransaction(
            InventoryTransaction(
                type = TransactionType.RESTOCK,
                productId = 1,
                quantity = 20,
                notes = "Initial stock"
            ).toEntity()
        )
    }
}