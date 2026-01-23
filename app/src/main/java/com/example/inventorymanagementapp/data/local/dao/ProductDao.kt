package com.example.inventorymanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inventorymanagementapp.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    // CREATE / UPDATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProduct(product: ProductEntity): Long

    @Query(
        """
    UPDATE products 
    SET current_stock = :newStock 
    WHERE id = :productId
    """
    )
    suspend fun updateStock(productId: Long, newStock: Int)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    // READ
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Long): ProductEntity?

    // SEARCH
    @Query(
        """
        SELECT * FROM products 
        WHERE name LIKE '%' || :query || '%' 
           OR barcode LIKE '%' || :query || '%'
        ORDER BY name ASC
    """
    )
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    // LOW STOCK
    @Query(
        """
        SELECT * FROM products
        WHERE current_stock <= minimum_stock
        ORDER BY current_stock ASC
    """
    )
    fun getLowStockProducts(): Flow<List<ProductEntity>>
}