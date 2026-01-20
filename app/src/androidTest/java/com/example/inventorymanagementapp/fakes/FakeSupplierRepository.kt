package com.example.inventorymanagementapp.fakes

import com.example.inventorymanagementapp.domain.model.Supplier
import com.example.inventorymanagementapp.domain.repository.SupplierRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FakeSupplierRepository @Inject constructor() : SupplierRepository {
    override fun getAllSuppliers(): Flow<List<Supplier>> {
        TODO("Not yet implemented")
    }

    override fun searchSuppliers(query: String): Flow<List<Supplier>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSupplierById(id: Long): Supplier? {
        TODO("Not yet implemented")
    }

    override suspend fun upsertSupplier(supplier: Supplier) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSupplier(supplier: Supplier) {
        TODO("Not yet implemented")
    }
}