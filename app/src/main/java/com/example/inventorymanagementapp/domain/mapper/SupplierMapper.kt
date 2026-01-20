package com.example.inventorymanagementapp.domain.mapper

import com.example.inventorymanagementapp.data.local.entity.SupplierEntity
import com.example.inventorymanagementapp.domain.model.Supplier

fun SupplierEntity.toDomain(): Supplier =
    Supplier(
        id = id,
        name = name,
        contactPerson = contactPerson,
        phone = phone,
        email = email,
        address = address
    )

fun Supplier.toEntity(): SupplierEntity =
    SupplierEntity(
        id = id,
        name = name,
        contactPerson = contactPerson,
        phone = phone,
        email = email,
        address = address
    )