package com.example.inventorymanagementapp.domain.model

data class Supplier(
    val id: Long = 0,
    val name: String,
    val contactPerson: String?,
    val phone: String?,
    val email: String?,
    val address: String?
)
