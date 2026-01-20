package com.example.inventorymanagementapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.inventorymanagementapp.domain.model.TransactionType
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class InventoryTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Long = System.currentTimeMillis(),
    val type: TransactionType,
    @ColumnInfo(name = "product_id")
    val productId: Long,
    val quantity: Int,
    val notes : String? = null,
    val createdAt: Long = System.currentTimeMillis()
): Parcelable
