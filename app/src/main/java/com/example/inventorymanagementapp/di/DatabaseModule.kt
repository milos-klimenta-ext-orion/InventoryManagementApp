package com.example.inventorymanagementapp.di

import android.content.Context
import androidx.room.Room
import com.example.inventorymanagementapp.data.local.InventoryDatabase
import com.example.inventorymanagementapp.data.local.dao.ProductDao
import com.example.inventorymanagementapp.data.local.dao.SupplierDao
import com.example.inventorymanagementapp.data.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): InventoryDatabase =
        Room.databaseBuilder(
            context,
            InventoryDatabase::class.java,
            "inventory_db"
        ).build()

    @Provides
    fun provideProductDao(db: InventoryDatabase): ProductDao =
        db.productDao()

    @Provides
    fun provideSupplierDao(db: InventoryDatabase): SupplierDao =
        db.supplierDao()

    @Provides
    fun provideTransactionDao(db: InventoryDatabase): TransactionDao =
        db.transactionDao()
}