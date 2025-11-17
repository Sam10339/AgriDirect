package com.example.agridirect20

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.getField
import kotlinx.coroutines.tasks.await

object FirebaseVenueRepository {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val venuesCollection = db.collection("venues")

    /**
     * Create a venue from Firestore.
     */
    suspend fun createVenue(
        name: String,
        address: GeoPoint,
    ): Venue {
        val userId = AuthManager.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in to create a venue")

        val venueData = hashMapOf(
            "name" to name.trim(),
            "address" to address,
        )

        val venueRef = venuesCollection.add(venueData).await()
        /**
         * Holding onto this for as a reference for adding vendors to venues.
         */
//        val productDocs = mutableListOf<Product>()
//        for (product in products) {
//            val productData = hashMapOf(
//                "name" to product.name.trim(),
//                "description" to product.description.trim(),
//                "price" to product.price,
//                "amount" to product.amount
//            )
//            val prodRef = farmRef.collection("products").add(productData).await()
//            productDocs.add(product.copy(id = prodRef.id))
//        }

        return Venue(
            id = venueRef.id,
            name = name.trim(),
            address = address,
        )
    }

    suspend fun getVenuesForCurrentUser(): List<Venue> {
        val userId = AuthManager.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in")

        val snapshot = venuesCollection
            .whereEqualTo("createdByUserId", userId)
            .get()
            .await()

        return snapshot.documents.map { doc ->
            Venue(
                id = doc.id,
                name = doc.getString("name") ?: "",
                address = doc.getField("address") ?: GeoPoint(0.0, 0.0)
            )
        }
    }

    /**
     * Get all venues for.
     */
    suspend fun getVenues(): List<Venue> {
        val snapshot = venuesCollection
            .get()
            .await()

        return snapshot.documents.map { doc ->
            Venue(
                id = doc.id,
                name = doc.getString("name") ?: "",
                address = doc.getField("address") ?: GeoPoint(0.0, 0.0)
            )
        }
    }
}