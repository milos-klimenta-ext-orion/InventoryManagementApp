package com.example.inventorymanagementapp.di

import com.example.inventorymanagementapp.domain.repository.ProductRepository
import com.example.inventorymanagementapp.domain.repository.SupplierRepository
import com.example.inventorymanagementapp.domain.repository.TransactionRepository
import com.example.inventorymanagementapp.fakes.FakeProductRepository
import com.example.inventorymanagementapp.fakes.FakeSupplierRepository
import com.example.inventorymanagementapp.fakes.FakeTransactionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object TestRepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(): ProductRepository = FakeProductRepository()

    @Provides
    @Singleton
    fun provideSupplierRepository(): SupplierRepository = FakeSupplierRepository()

    @Provides
    @Singleton
    fun provideTransactionRepository(): TransactionRepository = FakeTransactionRepository()
}