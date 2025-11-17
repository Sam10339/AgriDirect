package com.example.agridirect20

import com.google.firebase.firestore.GeoPoint

/**
 * Represents a product stored in a farm's Firestore subcollection.
 *
 * @property id Firestore document ID for this product.
 * @property name Name of the product (e.g., “Tomatoes”).
 * @property description Short description of the item.
 * @property price Price per unit.
 * @property amount Current stock quantity.
 *
 * USAGE EXAMPLE:
 *
 * val product = Product(
 *     id = "p1",
 *     name = "Carrots",
 *     description = "Fresh organic carrots",
 *     price = 1.49,
 *     amount = 20
 * )
 *
 */
data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val amount: Int = 0
)

/**
 * Represents a farm registered in Firestore.
 * Each farm may contain a list of products.
 *
 * @property id Firestore document ID for the farm.
 * @property name The farm's name.
 * @property description A short description or mission statement.
 * @property zip ZIP code used for filtering farms by region.
 * @property createdByUserId UID of the user who created this farm.
 * @property products List of Product objects belonging to this farm.
 *
 * USAGE EXAMPLE:
 *
 * val farm = Farm(
 *     id = "f1",
 *     name = "Green Valley Farm",
 *     description = "Local organic grower",
 *     zip = "53144",
 *     createdByUserId = "user123",
 *     products = listOf(product)
 * )
 *
 */
data class Farm(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val zip: String = "",
    val createdByUserId: String = "",
    val products: List<Product> = emptyList()
)

/**
 * Represents a market venue (used for future map + vendor features).
 *
 * @property id Firestore document ID for this venue.
 * @property name The venue’s displayed name.
 * @property address A GeoPoint storing latitude/longitude coordinates.
 *
 * USAGE EXAMPLE:
 *
 * val venue = Venue(
 *     id = "v1",
 *     name = "Kenosha Farmers Market",
 *     address = GeoPoint(42.5847, -87.8212)
 * )
 *
 */
data class Venue(
    val id: String = "",
    val name: String = "",
    val address: GeoPoint = GeoPoint(0.0, 0.0)
)
