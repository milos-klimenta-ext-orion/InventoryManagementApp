package com.example.inventorymanagementapp.di

import com.example.inventorymanagementapp.data.repository.ProductRepositoryImpl
import com.example.inventorymanagementapp.data.repository.SupplierRepositoryImpl
import com.example.inventorymanagementapp.data.repository.TransactionRepositoryImpl
import com.example.inventorymanagementapp.domain.repository.ProductRepository
import com.example.inventorymanagementapp.domain.repository.SupplierRepository
import com.example.inventorymanagementapp.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindSupplierRepository(
        impl: SupplierRepositoryImpl
    ): SupplierRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl
    ): TransactionRepository
}