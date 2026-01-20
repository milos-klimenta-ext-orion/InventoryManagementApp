package com.example.inventorymanagementapp.fakes

import com.example.inventorymanagementapp.domain.model.Supplier
import com.example.inventorymanagementapp.domain.repository.SupplierRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSupplierRepository : SupplierRepository {
    private val _suppliers = MutableStateFlow<List<Supplier>>(emptyList())

    override fun getAllSuppliers(): Flow<List<Supplier>> = _suppliers

    override fun searchSuppliers(query: String): Flow<List<Supplier>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSupplierById(id: Long): Supplier? {
        return _suppliers.value.firstOrNull { it.id == id }
            ?: throw Exception("Supplier not found")
    }

    override suspend fun upsertSupplier(supplier: Supplier) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSupplier(supplier: Supplier) {
        TODO("Not yet implemented")
    }

    fun emitSuppliers(list: List<Supplier>) {
        _suppliers.value = list
    }
}
