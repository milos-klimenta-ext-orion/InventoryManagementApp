package com.example.inventorymanagementapp.domain.repository

import com.example.inventorymanagementapp.domain.model.Supplier
import kotlinx.coroutines.flow.Flow

interface SupplierRepository {
    fun getAllSuppliers(): Flow<List<Supplier>>
    fun searchSuppliers(query: String): Flow<List<Supplier>>
    suspend fun getSupplierById( id: Long): Supplier?
    suspend fun upsertSupplier(supplier: Supplier)
    suspend fun deleteSupplier(supplier: Supplier)
}