package com.example.inventorymanagementapp.data.local.converters

import androidx.room.TypeConverter
import com.example.inventorymanagementapp.domain.model.TransactionType

class Converters {
    @TypeConverter
    fun fromTransactionType(type: TransactionType): String {
        return type.name
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }
}