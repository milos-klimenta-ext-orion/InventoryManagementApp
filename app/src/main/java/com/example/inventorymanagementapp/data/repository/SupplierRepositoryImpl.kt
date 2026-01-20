package com.example.inventorymanagementapp.data.repository

import com.example.inventorymanagementapp.data.local.dao.SupplierDao
import com.example.inventorymanagementapp.domain.mapper.toDomain
import com.example.inventorymanagementapp.domain.mapper.toEntity
import com.example.inventorymanagementapp.domain.model.Supplier
import com.example.inventorymanagementapp.domain.repository.SupplierRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SupplierRepositoryImpl @Inject constructor(
    private val supplierDao: SupplierDao
): SupplierRepository {

    override fun getAllSuppliers(): Flow<List<Supplier>> {
        return supplierDao.getAllSuppliers().map { it.map { entity -> entity.toDomain() } }
    }

    override fun searchSuppliers(query: String): Flow<List<Supplier>> {
        return supplierDao.searchSuppliers(query).map {
            it.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun getSupplierById(id: Long): Supplier? {
        return supplierDao.getSupplierById(id)?.toDomain()
    }

    override suspend fun upsertSupplier(supplier: Supplier) {
        supplierDao.upsertSupplier(supplier.toEntity())
    }

    override suspend fun deleteSupplier(supplier: Supplier) {
        supplierDao.deleteSupplier(supplier.toEntity())
    }
}