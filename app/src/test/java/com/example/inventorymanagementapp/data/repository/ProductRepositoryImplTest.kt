package com.example.inventorymanagementapp.data.repository

import com.example.inventorymanagementapp.data.local.entity.ProductEntity
import com.example.inventorymanagementapp.domain.mapper.toDomain
import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.fakes.FakeProductDao
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ProductRepositoryImplTest {

    private lateinit var dao: FakeProductDao
    private lateinit var repository: ProductRepositoryImpl

    private val entities = listOf(
        ProductEntity(
            id = 1,
            name = "Milk",
            price = 1.5,
            category = "Food",
            barcode = "123",
            supplierId = 1L,
            currentStock = 5,
            minimumStock = 10
        ),
        ProductEntity(
            id = 2,
            name = "Bread",
            price = 1.0,
            category = "Food",
            barcode = "999",
            supplierId = 1L,
            currentStock = 20,
            minimumStock = 5
        )
    )

    @Before
    fun setup() {
        dao = FakeProductDao()
        repository = ProductRepositoryImpl(dao)
    }

    @Test
    fun getAllProductsMapsEntitiesToDomain() = runTest {
        dao.emit(entities)

        val result = repository.getAllProducts().first()

        assertEquals(2, result.size)
        assertEquals("Milk", result[0].name)
    }

    @Test
    fun searchProductsFiltersCorrectly() = runTest {
        dao.emit(entities)

        val result = repository.searchProducts("milk").first()

        assertEquals(1, result.size)
        assertEquals("Milk", result.first().name)
    }

    @Test
    fun getLowStockProductsReturnsOnlyLowStock() = runTest {
        dao.emit(entities)

        val result = repository.getLowStockProducts().first()

        assertEquals(1, result.size)
        assertEquals("Milk", result.first().name)
    }

    @Test
    fun getProductByIdReturnsMappedProduct() = runTest {
        dao.emit(entities)

        val product = repository.getProductById(2)

        assertNotNull(product)
        assertEquals("Bread", product.name)
    }

    @Test
    fun upsertProductStoresMappedEntity() = runTest {
        val product = Product(
            id = 3,
            name = "Butter",
            price = 2.5,
            category = "Food",
            barcode = "555",
            supplierId = 1L,
            currentStock = 3,
            minimumStock = 5,
            description = null
        )

        repository.upsertProduct(product)

        val stored = dao.getProductById(3)
        assertNotNull(stored)
        assertEquals("Butter", stored.name)
    }

    @Test
    fun deleteProductRemovesEntity() = runTest {
        dao.emit(entities)

        repository.deleteProduct(entities.first().toDomain())

        val result = repository.getAllProducts().first()
        assertEquals(1, result.size)
    }
}