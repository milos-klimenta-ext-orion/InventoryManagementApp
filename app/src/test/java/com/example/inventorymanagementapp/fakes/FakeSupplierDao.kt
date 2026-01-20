package com.example.inventorymanagementapp.fakes

import com.example.inventorymanagementapp.data.local.dao.SupplierDao
import com.example.inventorymanagementapp.data.local.entity.SupplierEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeSupplierDao : SupplierDao {

    private val suppliers = mutableListOf<SupplierEntity>()
    private val flow = MutableStateFlow<List<SupplierEntity>>(emptyList())

    fun emit(list: List<SupplierEntity>) {
        suppliers.clear()
        suppliers.addAll(list)
        flow.value = list
    }

    override fun getAllSuppliers(): Flow<List<SupplierEntity>> = flow

    override fun searchSuppliers(query: String): Flow<List<SupplierEntity>> =
        flow.map {
            it.filter { supplier ->
                supplier.name.contains(query, ignoreCase = true)
            }
        }

    override suspend fun getSupplierById(id: Long): SupplierEntity? =
        suppliers.firstOrNull { it.id == id }

    override suspend fun upsertSupplier(supplier: SupplierEntity): Long {
        suppliers.removeAll { it.id == supplier.id }
        suppliers.add(supplier)
        flow.value = suppliers.toList()
        return 1L
    }

    override suspend fun deleteSupplier(supplier: SupplierEntity) {
        suppliers.removeIf { it.id == supplier.id }
        flow.value = suppliers.toList()
    }
}
