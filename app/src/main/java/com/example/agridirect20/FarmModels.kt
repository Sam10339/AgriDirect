package com.example.agridirect20

data class Product(
    val id: String = "",          // Firestore doc id
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val amount: Int = 0           // stock / quantity
)

data class Farm(
    val id: String = "",          // Firestore doc id
    val name: String = "",
    val description: String = "",
    val zip: String = "",
    val createdByUserId: String = "",
    val products: List<Product> = emptyList()
)
