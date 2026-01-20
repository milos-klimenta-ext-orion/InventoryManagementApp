package com.example.inventorymanagementapp.data.repository

import com.example.inventorymanagementapp.data.local.entity.SupplierEntity
import com.example.inventorymanagementapp.domain.mapper.toDomain
import com.example.inventorymanagementapp.domain.model.Supplier
import com.example.inventorymanagementapp.fakes.FakeSupplierDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class SupplierRepositoryImplTest {

    private lateinit var dao: FakeSupplierDao
    private lateinit var repository: SupplierRepositoryImpl

    private val entities = listOf(
        SupplierEntity(
            id = 1,
            name = "Supplier A",
            email = "contact@a.com",
            contactPerson = "Peter"
        ),
        SupplierEntity(
            id = 2,
            name = "Supplier B",
            email = "contact@b.com",
            contactPerson = "John"
        )
    )

    @Before
    fun setup() {
        dao = FakeSupplierDao()
        repository = SupplierRepositoryImpl(dao)
    }

    @Test
    fun getAllSuppliersMapsEntitiesToDomain() = runTest {
        dao.emit(entities)

        val result = repository.getAllSuppliers().first()

        assertEquals(2, result.size)
        assertEquals("Supplier A", result.first().name)
    }

    @Test
    fun searchSuppliersFiltersCorrectly() = runTest {
        dao.emit(entities)

        val result = repository.searchSuppliers("b").first()

        assertEquals(1, result.size)
        assertEquals("Supplier B", result.first().name)
    }

    @Test
    fun getSupplierByIdReturnsMappedSupplier() = runTest {
        dao.emit(entities)

        val supplier = repository.getSupplierById(2)

        assertNotNull(supplier)
        assertEquals("Supplier B", supplier.name)
    }

    @Test
    fun upsertSupplierStoresMappedEntity() = runTest {
        val supplier = Supplier(
            id = 3,
            name = "Supplier C",
            email = "contact@c.com",
            contactPerson = "Richard",
            phone = "12345",
            address = "Stret 1"
        )

        repository.upsertSupplier(supplier)

        val stored = dao.getSupplierById(3)

        assertNotNull(stored)
        assertEquals("Supplier C", stored.name)
    }

    @Test
    fun deleteSupplierRemovesEntity() = runTest {
        dao.emit(entities)

        repository.deleteSupplier(entities.first().toDomain())

        val result = repository.getAllSuppliers().first()
        assertEquals(1, result.size)
    }
}