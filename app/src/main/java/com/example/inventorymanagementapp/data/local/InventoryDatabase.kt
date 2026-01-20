package com.example.inventorymanagementapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.inventorymanagementapp.data.local.converters.Converters
import com.example.inventorymanagementapp.data.local.dao.ProductDao
import com.example.inventorymanagementapp.data.local.dao.SupplierDao
import com.example.inventorymanagementapp.data.local.dao.TransactionDao
import com.example.inventorymanagementapp.data.local.entity.InventoryTransactionEntity
import com.example.inventorymanagementapp.data.local.entity.ProductEntity
import com.example.inventorymanagementapp.data.local.entity.SupplierEntity

@Database(
    entities = [
        ProductEntity::class,
        SupplierEntity::class,
        InventoryTransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class InventoryDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun supplierDao(): SupplierDao
    abstract fun transactionDao(): TransactionDao
}