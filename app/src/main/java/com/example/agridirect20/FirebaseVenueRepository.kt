package com.example.agridirect20

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.tasks.await

object FirebaseVenueRepository {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val venuesCollection = db.collection("venues")

    /**
     * Create a venue in Firestore.
     */
    suspend fun createVenue(
        name: String,
        address: LatLng,
    ): Venue {
        val userId = AuthManager.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in to create a venue")

        val venueData = hashMapOf(
            "name" to name.trim(),
            "address" to GeoPoint(address.latitude, address.longitude),
            "createdByUserId" to userId
        )

        val venueRef = venuesCollection.add(venueData).await()

        return Venue(
            id = venueRef.id,
            name = name.trim(),
            address = address
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
            val geo = doc.getGeoPoint("address") ?: GeoPoint(0.0, 0.0)
            Venue(
                id = doc.id,
                name = doc.getString("name") ?: "",
                address = LatLng(geo.latitude, geo.longitude)
            )
        }
    }

    /**
     * Get all venues (for map + list).
     */
    suspend fun getVenues(): List<Venue> {
        val snapshot = venuesCollection
            .get()
            .await()

        return snapshot.documents.map { doc ->
            val geo = doc.getGeoPoint("address") ?: GeoPoint(0.0, 0.0)
            Venue(
                id = doc.id,
                name = doc.getString("name") ?: "",
                address = LatLng(geo.latitude, geo.longitude)
            )
        }
    }

    /**
     * Get a single venue by id (for MarketDetails screen).
     */
    suspend fun getVenueById(venueId: String): Venue? {
        if (venueId.isBlank()) return null

        val doc = venuesCollection
            .document(venueId)
            .get()
            .await()

        if (!doc.exists()) return null

        val geo = doc.getGeoPoint("address") ?: GeoPoint(0.0, 0.0)

        return Venue(
            id = doc.id,
            name = doc.getString("name") ?: "",
            address = LatLng(geo.latitude, geo.longitude)
        )
    }
}