package com.example.inventorymanagementapp.domain.mapper

import com.example.inventorymanagementapp.data.local.entity.ProductEntity
import com.example.inventorymanagementapp.domain.model.Product

fun ProductEntity.toDomain(): Product =
    Product(
        id = id,
        name = name,
        description = description,
        price = price,
        category = category,
        barcode = barcode,
        supplierId = supplierId,
        currentStock = currentStock,
        minimumStock = minimumStock
    )

fun Product.toEntity(): ProductEntity =
    ProductEntity(
        id = id,
        name = name,
        description = description,
        price = price,
        category = category,
        barcode = barcode,
        supplierId = supplierId,
        currentStock = currentStock,
        minimumStock = minimumStock
    )