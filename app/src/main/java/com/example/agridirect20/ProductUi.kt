package com.example.agridirect20

/**
 * ProductUi
 *
 * A  UI-only model used when displaying products
 * (for example, in FarmDetailsScreen or when adding items to the cart).
 *
 * This separates UI concerns from the full Product Firestore model.
 *
 * Usage Example:
 * val uiProduct = ProductUi(
 *     farmId = farm.id,
 *     productId = product.id,
 *     name = product.name,
 *     price = product.price
 * )
 *
 * onAddToCart(uiProduct)
 */
data class ProductUi(
    val farmId: String,
    val productId: String,
    val name: String,
    val price: Double
)
