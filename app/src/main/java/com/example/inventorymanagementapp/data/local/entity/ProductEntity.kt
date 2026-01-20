package com.example.inventorymanagementapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "products")
data class ProductEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val price: Double,
    val category: String? = null,
    val barcode: String? = null,

    @ColumnInfo(name = "supplier_id")
    val supplierId: Long,

    @ColumnInfo(name = "current_stock")
    val currentStock: Int,

    @ColumnInfo(name = "minimum_stock")
    val minimumStock: Int
) : Parcelable
